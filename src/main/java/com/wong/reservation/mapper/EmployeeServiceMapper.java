package com.wong.reservation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wong.reservation.domain.entity.EmployeeService;
import com.wong.reservation.domain.vo.RecommendServiceVO;

import java.util.List;

/**
* @author Wongbuer
* @description 针对表【employee_service】的数据库操作Mapper
* @createDate 2024-04-17 17:18:21
* @Entity com.wong.reservation.domain.entity.EmployeeService
*/
public interface EmployeeServiceMapper extends BaseMapper<EmployeeService> {
    List<RecommendServiceVO> selectRecommendServiceList();
}




