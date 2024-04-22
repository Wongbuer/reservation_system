package com.wong.reservation.controller;

import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.ChatLog;
import com.wong.reservation.service.ChatLogService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Wongbuer
 * @createDate 2024/4/22
 */
@RestController
@RequestMapping(value = "/chat", method = {RequestMethod.GET, RequestMethod.POST})
public class ChatLogController {
    @Resource
    private ChatLogService chatLogService;

    /**
     * 获取聊天记录
     *
     * @param otherUserId 另一位用户Id
     * @return 聊天记录
     */
    @Operation(summary = "获取聊天记录")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<List<ChatLog>> getChatLogList(@RequestParam("id") Long otherUserId) {
        return chatLogService.getChatLogList(otherUserId);
    }

    /**
     * 设置已读
     *
     * @param chatLogIdList 聊天记录Id列表
     * @return 设置结果
     */
    @Operation(summary = "设置已读")
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public Result<?> setRead(List<Long> chatLogIdList) {
        return chatLogService.setRead(chatLogIdList);
    }
}
