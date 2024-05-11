package com.wong.reservation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.User;

/**
 * @author Wongbuer
 * @description 针对表【user】的数据库操作Service
 * @createDate 2024-04-09 22:18:25
 */
public interface UserService extends IService<User> {
    /**
     * 根据登录Token获取用户信息
     *
     * @return Result<User>
     */
    Result<User> getUserInfoByToken();

    /**
     * 根据员工ID获取用户信息
     *
     * @param employeeId 员工ID
     * @return Result<User>
     */
    Result<User> getUserInfoByEmployeeId(Long employeeId);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return Result<?> 更新结果
     */
    Result<?> updateUserInfo(User user);
}
