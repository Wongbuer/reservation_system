package com.wong.reservation.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

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
    @TableId(value = "id")
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
    private String access_token;
    /**
     *
     */
    @TableField(value = "refresh_token")
    private String refresh_token;

    public SocialUser(String uuid, String source, String access_token) {
        this.uuid = uuid;
        this.source = source;
        this.access_token = access_token;
    }
}