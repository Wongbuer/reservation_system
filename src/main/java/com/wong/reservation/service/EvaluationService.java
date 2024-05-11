package com.wong.reservation.service;

import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Evaluation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Wongbuer
* @description 针对表【evaluation】的数据库操作Service
* @createDate 2024-04-20 22:22:06
*/
public interface EvaluationService extends IService<Evaluation> {

    /**
     * 增加订单评价
     *
     * @param evaluation 订单评价
     * @return 增加评价结果
     */
    Result<?> addEvaluation(Evaluation evaluation);

    /**
     * 根据订单ID获取评价信息
     *
     * @param orderId 订单ID
     * @return Result<Evaluation> 订单评价
     */
    Result<Evaluation> getEvaluationByOrderId(Long orderId);
}
