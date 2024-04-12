package com.wong.reservation.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Wongbuer
 * @TableName social_user_auth
 */
@TableName(value = "social_user_auth")
@Data
public class SocialUserAuth implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 系统用户id
     */
    @TableField(value = "user_id")
    private Long userId;
    /**
     * 社会化用户id
     */
    @TableField(value = "social_user_id")
    private Long socialUserId;
    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;
    /**
     * 更新时间
     */
    @TableField(value = "update_at")
    private LocalDateTime updateAt;
}