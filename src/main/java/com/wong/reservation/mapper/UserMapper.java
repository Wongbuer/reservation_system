package com.wong.reservation.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wong.reservation.domain.entity.User;

/**
 * @author Wongbuer
 * @description 针对表【user】的数据库操作Mapper
 * @createDate 2024-04-09 22:18:25
 * @Entity com.wong.reservation.domain.entity.User
 */
public interface UserMapper extends BaseMapper<User> {
    User selectUserByEmployeeId(Long employeeId);
}




