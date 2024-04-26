package com.wong.reservation.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wong.reservation.domain.entity.Employee;
import com.wong.reservation.domain.entity.Service;
import com.wong.reservation.domain.entity.User;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Wongbuer
 * @createDate 2024/4/25
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeServiceVO {
    private Long id;
    private Employee employee;
    private User user;
    private Service service;
    private Integer rate;
    private BigDecimal price;
    private BigDecimal discount;
    private String timeUint;
    private String detailedPictures;
}
