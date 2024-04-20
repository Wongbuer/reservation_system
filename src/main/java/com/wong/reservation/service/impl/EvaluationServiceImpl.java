package com.wong.reservation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wong.reservation.constant.OrderStatusConstant;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Evaluation;
import com.wong.reservation.domain.entity.Order;
import com.wong.reservation.mapper.EvaluationMapper;
import com.wong.reservation.mapper.OrderMapper;
import com.wong.reservation.service.EvaluationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author Wongbuer
 * @description 针对表【evaluation】的数据库操作Service实现
 * @createDate 2024-04-20 22:22:06
 */
@Service
public class EvaluationServiceImpl extends ServiceImpl<EvaluationMapper, Evaluation> implements EvaluationService {
    @Resource
    private OrderMapper orderMapper;

    @Override
    public Result<?> addEvaluation(Evaluation evaluation) {
        // 判断参数是否合法
        if (evaluation.getOrderId() == null || evaluation.getContent() == null) {
            return Result.fail("评价信息有误");
        }
        Order order = orderMapper.selectById(evaluation.getOrderId());
        if (ObjectUtils.isEmpty(order)) {
            return Result.fail("订单不存在");
        }
        if (order.getStatus() == OrderStatusConstant.EVALUATED) {
            return Result.fail("订单已评价");
        }
        boolean isSaved = save(evaluation);
        return isSaved ? Result.success("评价成功") : Result.fail("评价失败");
    }
}






