package com.wong.reservation.domain.dto;

import lombok.Data;

/**
 * @author Wongbuer
 */
@Data
public class AliPayDTO {
    private String traceNo;
    private double totalAmount;
    private String subject;
}

