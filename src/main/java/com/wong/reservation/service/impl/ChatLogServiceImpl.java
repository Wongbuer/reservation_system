package com.wong.reservation.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wong.reservation.constant.ChatLogStatusConstant;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.ChatLog;
import com.wong.reservation.domain.entity.User;
import com.wong.reservation.mapper.ChatLogMapper;
import com.wong.reservation.service.ChatLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Wongbuer
* @description 针对表【chat_log】的数据库操作Service实现
* @createDate 2024-04-22 14:40:36
*/
@Service
public class ChatLogServiceImpl extends ServiceImpl<ChatLogMapper, ChatLog> implements ChatLogService{
    @Override
    public Result<List<ChatLog>> getChatLogList(Long otherUserId) {
        List<Long> userList = List.of(otherUserId, StpUtil.getLoginIdAsLong());
        LambdaQueryWrapper<ChatLog> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .in(ChatLog::getSenderId, userList)
                .in(ChatLog::getReceiverId, userList)
                .orderBy(true, true, ChatLog::getCreatedAt);
        try {
            List<ChatLog> list = list(wrapper);
            return Result.success(list);
        } catch (Exception e) {
            return Result.fail("获取聊天记录失败");
        }
    }

    @Override
    public Result<?> setRead(List<Long> chatLogIdList) {
        LambdaUpdateWrapper<ChatLog> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(ChatLog::getId, chatLogIdList)
                .set(ChatLog::getStatus, ChatLogStatusConstant.READ);
        boolean isUpdated;
        try {
            isUpdated = update(wrapper);
        } catch (Exception e) {
            return Result.fail("设置已读失败");
        }
        return isUpdated ? Result.success("设置已读成功") : Result.fail("设置已读失败");
    }

    @Override
    public Result<List<User>> getChatUserList() {
        long userId = StpUtil.getLoginIdAsLong();
        List<User> userList = null;
        try {
            userList = baseMapper.selectChatUserList(userId);
        } catch (Exception e) {
            return Result.fail("获取聊天用户列表失败");
        }
        return Result.success(userList);
    }
}




