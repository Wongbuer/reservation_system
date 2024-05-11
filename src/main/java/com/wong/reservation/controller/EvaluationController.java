package com.wong.reservation.controller;

import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Evaluation;
import com.wong.reservation.service.EvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wongbuer
 * @createDate 2024/5/4
 */
@RestController
@RequestMapping(value = "/evaluation", method = {RequestMethod.GET, RequestMethod.POST})
public class EvaluationController {
    @Resource
    private EvaluationService evaluationService;

    /**
     * 根据订单ID获取评价信息
     *
     * @param orderId 订单ID
     * @return Result<Evaluation> 订单评价
     */
    @Operation(summary = "根据订单ID获取评价信息")
    @RequestMapping(value = "/getEvaluationByOrderId", method = RequestMethod.GET)
    public Result<Evaluation> getEvaluationByOrderId(Long orderId) {
        return evaluationService.getEvaluationByOrderId(orderId);
    }
}
