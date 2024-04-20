package com.wong.reservation.service;

import com.wong.reservation.domain.dto.AliPayDTO;
import com.wong.reservation.domain.dto.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Wongbuer
 * @createDate 2024/4/20
 */
public interface AliPayService {
    void goToPay(AliPayDTO aliPayDTO, HttpServletResponse response) throws Exception;

    Result<?> afterPay(HttpServletRequest request);
}
