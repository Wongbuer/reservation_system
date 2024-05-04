package com.wong.reservation.controller;

import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.User;
import com.wong.reservation.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wongbuer
 * @createDate 2024/5/4
 */
@RestController
@RequestMapping(value = "/user", method = {RequestMethod.GET, RequestMethod.POST})
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 根据登录Token获取用户信息
     *
     * @return Result<User>
     */
    @Operation(summary = "根据登录Token获取用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Result<User> getUserInfoByToken() {
        return userService.getUserInfoByToken();
    }

    /**
     * 根据员工ID获取用户信息
     *
     * @param employeeId 员工ID
     * @return Result<User>
     */
    @Operation(summary = "根据员工ID获取用户信息")
    @RequestMapping(value = "/getUserInfoByEmployeeId", method = RequestMethod.GET)
    public Result<User> getUserInfoByEmployeeId(Long employeeId) {
        return userService.getUserInfoByEmployeeId(employeeId);
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return Result<?> 更新结果
     */
    @Operation(summary = "更新用户信息")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result<?> updateUserInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }
}
