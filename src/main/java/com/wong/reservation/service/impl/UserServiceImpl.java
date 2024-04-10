package com.wong.reservation.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wong.reservation.domain.entity.User;
import com.wong.reservation.mapper.UserMapper;
import com.wong.reservation.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Wongbuer
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-04-09 22:18:25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}




