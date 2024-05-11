package com.wong.reservation.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.User;
import com.wong.reservation.mapper.UserMapper;
import com.wong.reservation.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author Wongbuer
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-04-09 22:18:25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public Result<User> getUserInfoByToken() {
        long userId = StpUtil.getLoginIdAsLong();
        User user = getById(userId);
        return ObjectUtils.isEmpty(user) ? Result.fail("用户不存在") : Result.success(user);
    }

    @Override
    public Result<User> getUserInfoByEmployeeId(Long employeeId) {
        User user = baseMapper.selectUserByEmployeeId(employeeId);
        return ObjectUtils.isEmpty(user) ? Result.fail("用户不存在") : Result.success(user);
    }

    @Override
    public Result<?> updateUserInfo(User user) {
        long userId = StpUtil.getLoginIdAsLong();
        user.setId(userId);
        boolean isUpdated = updateById(user);
        return isUpdated ? Result.success("更新成功") : Result.fail("更新失败");
    }
}




