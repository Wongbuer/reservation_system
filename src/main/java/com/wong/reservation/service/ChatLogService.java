package com.wong.reservation.service;

import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.ChatLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Wongbuer
 * @description 针对表【chat_log】的数据库操作Service
 * @createDate 2024-04-22 14:40:36
 */
public interface ChatLogService extends IService<ChatLog> {
    /**
     * 获取聊天记录
     *
     * @param otherUserId 另一位用户Id
     * @return 聊天记录
     */
    Result<List<ChatLog>> getChatLogList(Long otherUserId);

    /**
     * 设置已读
     *
     * @param chatLogIdList 聊天记录Id列表
     * @return 设置结果
     */
    Result<?> setRead(List<Long> chatLogIdList);
}
