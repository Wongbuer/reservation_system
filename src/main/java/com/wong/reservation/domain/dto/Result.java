package com.wong.reservation.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {
    private static final String DEFAULT_SUCCESS_MSG = "success";
    private Integer code;
    private String msg;
    private T data;


    public Result(T data) {
        this.code = 20000;
        this.msg = DEFAULT_SUCCESS_MSG;
        this.data = data;
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<>(20001, msg);
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        return new Result<>(code, msg);
    }

    public static <T> Result<T> success(String msg) {
        return new Result<>(20000, msg);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(20000, data);
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(20000, msg, data);
    }

    public static Result<?> token() {
        return new Result<>(40001, "未能读取到有效token, 请登录");
    }

}
