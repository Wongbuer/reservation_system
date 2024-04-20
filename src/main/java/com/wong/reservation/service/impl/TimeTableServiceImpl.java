package com.wong.reservation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wong.reservation.domain.entity.TimeTable;
import com.wong.reservation.domain.entity.Order;
import com.wong.reservation.service.TimeTableService;
import com.wong.reservation.mapper.TimeTableMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
* @author Wongbuer
* @description 针对表【time_table(订单时间表)】的数据库操作Service实现
* @createDate 2024-04-19 23:15:17
*/
@Service
public class TimeTableServiceImpl extends ServiceImpl<TimeTableMapper, TimeTable> implements TimeTableService{
    @Override
    public TimeTable checkConflictAndCreateTimeTable(Order order) {
        return baseMapper.checkConflict(order);
    }
}




