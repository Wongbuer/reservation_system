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
 * @TableName social_user
 */
@TableName(value = "social_user")
@Data
public class SocialUser implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * auth主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 第三方唯一id 非null
     */
    @TableField(value = "uuid")
    private String uuid;
    /**
     * 认证类型 非null
     */
    @TableField(value = "source")
    private String source;
    /**
     *
     */
    @TableField(value = "access_token")
    private String accessToken;
    /**
     *
     */
    @TableField(value = "refresh_token")
    private String refreshToken;
    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;
    /**
     * 更新时间
     */
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;
    public SocialUser(String uuid, String source, String accessToken) {
        this.uuid = uuid;
        this.source = source;
        this.accessToken = accessToken;
    }
}