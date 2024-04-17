package com.wong.reservation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wong.reservation.domain.entity.Order;
import org.apache.ibatis.annotations.MapKey;

import java.util.Map;

/**
 * @author Wongbuer
 * @description 针对表【orders】的数据库操作Mapper
 * @createDate 2024-04-15 18:05:22
 * @Entity com.wong.reservation.domain.entity.Order
 */
public interface OrderMapper extends BaseMapper<Order> {
    Map<String, Long> isOrderInfoValid(Order order);
}




