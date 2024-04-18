package com.wong.reservation.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.wong.reservation.domain.dto.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 *
 * @author Wongbuer
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 未登录异常
     *
     * @param e NotLoginException
     * @return 未登录Result
     */
    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    public Result<?> notLoginExceptionHandler(NotLoginException e) {
        e.printStackTrace();
        return Result.token();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<?> exceptionHandler(Exception e) {
        e.printStackTrace();
        return Result.fail(50000, "系统异常");
    }
}
