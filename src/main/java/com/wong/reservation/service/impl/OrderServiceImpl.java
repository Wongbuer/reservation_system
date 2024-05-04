package com.wong.reservation.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wong.reservation.constant.OrderStatusConstant;
import com.wong.reservation.domain.dto.AliPayDTO;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Evaluation;
import com.wong.reservation.domain.entity.Order;
import com.wong.reservation.domain.entity.TimeTable;
import com.wong.reservation.mapper.EmployeeServiceMapper;
import com.wong.reservation.mapper.OrderMapper;
import com.wong.reservation.service.*;
import com.wong.reservation.utils.RedisUtils;
import com.wong.reservation.utils.lock.OrderLock;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.wong.reservation.constant.SystemConstant.*;

/**
 * @author Wongbuer
 * @description 针对表【orders】的数据库操作Service实现
 * @createDate 2024-04-15 18:05:22
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    private static SecretKey KEY = Keys.hmacShaKeyFor(QR_CODE_SHA256_SECRET.getBytes(StandardCharsets.UTF_8));
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private EmployeeService employeeService;
    @Resource
    private OrderLock orderLock;
    @Resource
    private ServiceService serviceService;
    @Resource
    private TimeTableService timeTableService;
    @Resource
    private EvaluationService evaluationService;
    @Resource
    private AliPayService aliPayService;
    @Resource
    private EmployeeServiceMapper employeeServiceMapper;
    @Resource
    private Snowflake snowflake;
    @Resource
    private PlatformTransactionManager transactionManager;

    @Override
    public Result<List<Order>> getOrderByUserId(String status, Boolean sort) {
        // 根据登录信息获取userId
        Long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        // 查询当前用户对应status下的所有订单, 如果status为null则为所有status的订单
        wrapper.eq(Order::getUserId, userId)
                .eq(StringUtils.hasText(status), Order::getStatus, status)
                // 未被删除的订单
                .eq(Order::getIsDeleted, 0)
                .orderByDesc(sort, Order::getCreatedAt);
        List<Order> orderList = null;
        try {
            orderList = list(wrapper);
        } catch (Exception e) {
            // TODO: 日志处理
            e.printStackTrace();
            return Result.fail("获取订单失败");
        }
        return Result.success("获取订单成功", orderList);
    }

    @Override
    public Result<?> createOrder(Order order) {
        // 从登录信息获取userId
        Long userId = StpUtil.getLoginIdAsLong();
        order.setUserId(userId);
        // 根据雪花算法生成orderId
        order.setId(snowflake.nextId());

        // 如果存在employeeServiceId, 则补全其他信息
        if (!ObjectUtils.isEmpty(order.getEmployeeServiceId())) {
            com.wong.reservation.domain.entity.EmployeeService info = employeeServiceMapper.selectById(order.getEmployeeServiceId());
            if (info != null) {
                order.setEmployeeId(info.getEmployeeId());
                order.setServiceId(info.getServiceId());
            }
        }

        // 判断order是否含有必要参数(地址ID是否存在/服务ID是否存在/用户ID是否存在/员工ID是否存在/时间是否合法)
        int validCode = isOrderInfoValid(order);
        if (validCode == -1) {
            return Result.fail(validCode, "订单信息不完整");
        }
        if ((validCode & 4) == 4) {
            return Result.fail(40000 + validCode, "订单服务有误");
        }
        if ((validCode & 2) == 2) {
            return Result.fail(40000 + validCode, "订单地址有误");
        }
        if ((validCode & 1) == 1) {
            return Result.fail(40000 + validCode, "订单预约时间有误");
        }

        // 判断是否含有相同的订单(相同指地址/服务/预约时间均相同)
        if (checkDuplicateOrder(order)) {
            return Result.fail("订单重复, 请勿重复下单");
        }

        String lockKey = ORDER_LOCK_CREATE_PREFIX + order.getEmployeeId();
        // 获取锁
        boolean isLocked = orderLock.acquireLock(lockKey, 5000);

        if (isLocked) {
            TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
            try {
                log.info("进入锁");
                // 判断金额是否正确
                if (!isOrderPriceValid(order)) {
                    return Result.fail("订单金额有误");
                }
                // 判断员工时间是否冲突
                TimeTable timeTable = timeTableService.checkConflictAndCreateTimeTable(order);
                if (ObjectUtils.isEmpty(timeTable)) {
                    return Result.fail(40000, "员工时间冲突");
                }
                // 设置订单状态为created
                order.setStatus(OrderStatusConstant.CREATED);
                // 添加订单
                save(order);
                // 添加时间表
                timeTable.setOrderId(order.getId());
                timeTableService.save(timeTable);
                // 添加订单到redis, 15分钟后过期, 到期未接受则改变订单状态
                redisUtils.set(ORDER_CREATED_PREFIX + order.getId(), order, 60 * 15);
                transactionManager.commit(status);
            } catch (Exception e) {
                // TODO: 日志处理
                e.printStackTrace();
                transactionManager.rollback(status);
                return Result.fail(50000, "添加订单失败");
            } finally {
                orderLock.releaseLock(lockKey);
            }
            return Result.success("添加订单成功", order);
        } else {
            return Result.fail("订单创建失败, 请稍后再试");
        }
    }

    @Override
    public Result<?> updateOrder(Order order) {
        // 判断是否含有orderID
        if (order.getId() == null) {
            return Result.fail(40000, "订单ID不能为空");
        }
        // 从数据库获取订单信息, 补全order
        Order oldOrder = getById(order.getId());
        try {
            copyProperties(order, oldOrder);
            // 判断订单修改后是否重复
            if (checkDuplicateOrder(order)) {
                return Result.fail("订单重复, 请勿重复下单");
            }
            // 修改订单
            updateById(oldOrder);
        } catch (IllegalAccessException e) {
            // TODO: 日志处理
            e.printStackTrace();
            return Result.fail(50000, "订单信息不完整");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(50000, "修改订单失败");
        }
        return Result.success("修改订单成功");
    }

    @Override
    public Result<?> deleteOrder(Long id) {
        // 根据登录信息获取userId
        long userId = StpUtil.getLoginIdAsLong();
        // 删除订单
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(Order::getUserId, userId)
                .eq(Order::getId, id);
        boolean isDeleted = remove(wrapper);
        return isDeleted ? Result.success("删除订单成功") : Result.fail("删除订单失败");
    }

    @Override
    public Result<?> acceptOrder(Long id) {
        // 获取employeeId
        long employeeId = employeeService.getEmployeeId();
        String lockKey = ORDER_LOCK_ACCEPT_PREFIX + id;
        boolean isLocked = orderLock.acquireLock(lockKey, 5000);
        if (isLocked) {
            try {
                // 获取订单
                Order order = getById(id);
                // 判断订单状态
                if (ObjectUtils.isEmpty(order)) {
                    return Result.fail(40000, "订单不存在");
                }
                if (order.getStatus() != OrderStatusConstant.CREATED) {
                    return Result.fail(40000, "订单状态错误");
                }
                // 修改订单状态
                LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
                wrapper
                        .eq(Order::getId, id)
                        .set(Order::getAcceptedTime, LocalDateTime.now())
                        .set(Order::getStatus, OrderStatusConstant.ACCEPTED);
                if (!update(wrapper)) {
                    return Result.fail(50000, "接受订单失败");
                } else {
                    // 设置redis过期时间(15分钟内未付款自动取消订单)
                    redisUtils.set(ORDER_ACCEPTED_PREFIX + id, order, 60 * 15);
                    return Result.success("接受订单成功");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                orderLock.releaseLock(lockKey);
            }
        } else {
            return Result.fail("接受订单失败, 请稍后再试");
        }
    }

    @Override
    public void payOrder(Long id, HttpServletResponse response) {
        // 加锁
        String lockKey = ORDER_LOCK_PAY_PREFIX + id;
        boolean isLocked = orderLock.acquireLock(lockKey, 5000);
        if (isLocked) {
            try {
                // 获取订单
                id = 1784291607348543488L;
                Order order = getById(id);
                if (ObjectUtils.isEmpty(order)) {
                    Result.fail(40000, "订单不存在", response);
                    return;
                }
                // 判断状态
                if (order.getStatus() != OrderStatusConstant.ACCEPTED) {
                    Result.fail(40000, "订单状态错误", response);
                    return;
                }
                // 生成支付信息(traceNo/totalAmount/subject)
                AliPayDTO aliPayDTO = new AliPayDTO();
                LocalDateTime reservationTime = order.getReservationTime();
                double totalAmount = order.getPayment().doubleValue();
                String serviceName = serviceService.getById(order.getServiceId()).getName();
                String traceNo = order.getId().toString();
                String subject = "预约" + serviceName + "服务" + " " + reservationTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                aliPayDTO.setSubject(subject);
                aliPayDTO.setTotalAmount(totalAmount);
                aliPayDTO.setTraceNo(traceNo);
                aliPayService.goToPay(aliPayDTO, response);
                Result.success("跳转至支付界面", response);
            } catch (Exception e) {
                Result.fail(50000, "订单支付失败", response);
            } finally {
                // 释放锁
                orderLock.releaseLock(lockKey);
            }
        } else {
            Result.fail(50000, "订单支付失败", response);
        }
    }

    @Override
    public void showQrCode(Long id, HttpServletResponse response) {
        String encodedString = Jwts
                .builder()
                .signWith(KEY)
                .claims(Map.of("orderId", id))
                .compact();
        ServletOutputStream out;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        QrCodeUtil.generate(encodedString, 300, 300, ImgUtil.IMAGE_TYPE_PNG, out);
    }

    @Override
    public Result<?> parseQrCode(String content) {
        Claims payload = (Claims) Jwts.parser()
                .verifyWith(KEY)
                .build().parse(content).getPayload();
        Long orderId = payload.get("orderId", Long.class);
        // 从数据库获取订单信息
        Order order = getById(orderId);
        if (ObjectUtils.isEmpty(order)) {
            return Result.fail(40000, "订单不存在");
        }
        // 判断订单状态
        LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Order::getId, orderId);
        if (order.getStatus() == OrderStatusConstant.PAID) {
            wrapper.set(Order::getStatus, OrderStatusConstant.PENDING);
        } else if (order.getStatus() == OrderStatusConstant.PENDING) {
            // 判断是否到服务结束时间
            TimeTable timeTable = timeTableService.getOne(new LambdaQueryWrapper<TimeTable>().eq(TimeTable::getOrderId, orderId));
            if (LocalDateTime.now().isBefore(timeTable.getEndTime())) {
                return Result.fail(40000, "服务未结束");
            }
            wrapper
                    .set(Order::getStatus, OrderStatusConstant.NOT_EVALUATED)
                    .set(Order::getEndTime, LocalDateTime.now());
        } else {
            return Result.fail(40000, "订单状态错误");
        }
        // 改变订单状态
        boolean isUpdated = update(wrapper);
        if (isUpdated && order.getStatus() == OrderStatusConstant.PENDING) {
            // 设置评价过期时间(24小时)
            redisUtils.set(ORDER_EVALUATE_PREFIX + orderId, order, 60 * 60 * 24);
        }
        // 返回处理结果
        return isUpdated ? Result.success("订单状态修改成功") : Result.fail(50000, "订单状态修改失败");
    }

    @Override
    public Result<?> evaluateOrder(Evaluation evaluation) {
        if (evaluation.getOrderId() == null) {
            return Result.fail(40000, "订单ID不能为空");
        }
        Result<?> result;
        String lockKey = ORDER_LOCK_EVALUATE_PREFIX + evaluation.getOrderId();
        boolean isLocked = orderLock.acquireLock(lockKey, 5000);
        if (isLocked) {
            TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
            try {
                result = evaluationService.addEvaluation(evaluation);
                LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
                wrapper
                        .eq(Order::getId, evaluation.getOrderId())
                        .set(Order::getStatus, OrderStatusConstant.EVALUATED);
                update(wrapper);
                transactionManager.commit(status);
            } catch (Exception e) {
                transactionManager.rollback(status);
                return Result.fail(50000, "订单评价失败");
            } finally {
                orderLock.releaseLock(lockKey);
            }
        } else {
            return Result.fail(50000, "订单评价失败");
        }
        return result;
        // TODO: 计算员工评分或相关逻辑
    }

    private boolean checkDuplicateOrder(Order order) {
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper
                // 地址相同
                .eq(Order::getAddressId, order.getAddressId())
                // 服务内容
                .eq(Order::getServiceId, order.getServiceId())
                // 员工ID相同
                .eq(Order::getEmployeeId, order.getEmployeeId())
                // 用户ID相同
                .eq(Order::getUserId, order.getUserId())
                // 预约时间相同
                .eq(Order::getReservationTime, order.getReservationTime())
                // 订单未删除
                .eq(Order::getIsDeleted, 0);
        return count(orderWrapper) > 0;
    }

    private void copyProperties(Order newOrder, Order oldOrder) throws IllegalAccessException {
        for (Field field : Order.class.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(newOrder) == null) {
                field.set(newOrder, field.get(oldOrder));
            }
        }
    }

    private int isOrderInfoValid(Order order) {
        // 判断是否存在地址/服务/用户/员工/预约时间
        if (order.getAddressId() == null || order.getServiceId() == null || order.getUserId() == null || order.getEmployeeId() == null || order.getReservationTime() == null) {
            return -1;
        }
        // 判断是否存在支付金额/支付方式/服务单位数
        if (ObjectUtils.isEmpty(order.getPayment()) || ObjectUtils.isEmpty(order.getPaymentType()) || ObjectUtils.isEmpty(order.getUnitCount())) {
            return -1;
        }
        Map<String, Long> map = baseMapper.isOrderInfoValid(order);
        int res = 0;
        if (map.get("employee_service_exists") == 0) {
            res += 4;
        }
        if (map.get("address_exists") == 0) {
            res += 2;
        }
        if (map.get("future_check") == 0) {
            res += 1;
        }
        return res;
    }

    private boolean isOrderPriceValid(Order order) {
        // 判断是否为空
        BigDecimal bigDecimal = baseMapper.getOrderPrice(order.getUnitCount(), order.getServiceId());
        return bigDecimal.compareTo(order.getPayment()) == 0;
    }
}




