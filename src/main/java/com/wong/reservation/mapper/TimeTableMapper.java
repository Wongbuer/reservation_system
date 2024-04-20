package com.wong.reservation.mapper;

import com.wong.reservation.domain.entity.TimeTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wong.reservation.domain.entity.Order;

/**
* @author Wongbuer
* @description 针对表【time_table(订单时间表)】的数据库操作Mapper
* @createDate 2024-04-19 23:15:17
* @Entity com.wong.reservation.domain.entity.TimeTable
*/
public interface TimeTableMapper extends BaseMapper<TimeTable> {
    /**
     * 检查该订单对应的员工时间表是否冲突, 如果不冲突则返回TimeTable对象
     *
     * @param order 订单
     * @return TimeTable 时间表
     */
    TimeTable checkConflict(Order order);
}




