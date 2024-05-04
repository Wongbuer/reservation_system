package com.wong.reservation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wong.reservation.domain.entity.ChatLog;
import com.wong.reservation.domain.entity.User;

import java.util.List;

/**
* @author Wongbuer
* @description 针对表【chat_log】的数据库操作Mapper
* @createDate 2024-04-22 14:40:36
* @Entity com.wong.reservation.domain.entity.ChatLog
*/
public interface ChatLogMapper extends BaseMapper<ChatLog> {
    List<User> selectChatUserList(Long userId);
}




