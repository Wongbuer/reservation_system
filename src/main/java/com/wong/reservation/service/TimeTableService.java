package com.wong.reservation.service;

import com.wong.reservation.domain.entity.TimeTable;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wong.reservation.domain.entity.Order;

/**
* @author Wongbuer
* @description 针对表【time_table(订单时间表)】的数据库操作Service
* @createDate 2024-04-19 23:15:17
*/
public interface TimeTableService extends IService<TimeTable> {
    /**
     * 检查该订单对应的员工时间表是否冲突, 如果不冲突则返回TimeTable对象
     *
     * @param order 订单
     * @return TimeTable 时间表
     */
    TimeTable checkConflictAndCreateTimeTable(Order order);
}
