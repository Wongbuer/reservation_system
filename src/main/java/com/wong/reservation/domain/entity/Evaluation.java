package com.wong.reservation.domain.entity;

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
 * @TableName evaluation
 */
@TableName(value = "evaluation")
@Data
public class Evaluation implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 用户评价表主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 订单id
     */
    @TableField(value = "order_id")
    private Long orderId;
    /**
     * 评价内容
     */
    @TableField(value = "content")
    private String content;
    /**
     * 订单分数(5分制)
     */
    @TableField(value = "score")
    private Integer score;
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