package com.wong.reservation.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Wongbuer
 */
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
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        timeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(timeModule);
    }


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

    public static void fail(String msg, HttpServletResponse response) {
        fail(20001, msg, response);
    }

    public static void fail(Integer code, String msg, HttpServletResponse response) {
        Result<Object> result = new Result<>(code, msg);
        try {
            String resultString = objectMapper.writeValueAsString(result);
            response.getWriter().write(resultString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        return new Result<>(code, msg);
    }

    public static <T> Result<T> success(String msg) {
        return new Result<>(20000, msg);
    }

    public static <T> void success(String msg, HttpServletResponse response) {
        success(20000, msg, response);
    }

    public static <T> void success(Integer code, String msg, HttpServletResponse response) {
        Result<Object> result = new Result<>(20000, msg);
        try {
            String resultString = objectMapper.writeValueAsString(result);
            response.getWriter().write(resultString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void success(T data, HttpServletResponse response) {
        Result<Object> result = new Result<>(20000, DEFAULT_SUCCESS_MSG, data);
        try {
            String resultString = objectMapper.writeValueAsString(result);
            response.getWriter().write(resultString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
