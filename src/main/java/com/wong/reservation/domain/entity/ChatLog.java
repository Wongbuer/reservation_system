package com.wong.reservation.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Wongbuer
 * @TableName chat_log
 */
@TableName(value = "chat_log")
@Data
public class ChatLog implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 聊天信息主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 发送方id(与接收方id组成unique键)
     */
    @TableField(value = "sender_id")
    private Long senderId;
    /**
     * 接收方id
     */
    @TableField(value = "receiver_id")
    private Long receiverId;
    /**
     * 发送内容
     */
    @TableField(value = "content")
    private String content;
    /**
     * 已读状态(0: 未读, 1: 已读)
     */
    @TableField(value = "status")
    private Integer status;
    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    /**
     * 更新时间
     */
    @TableField(value = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}