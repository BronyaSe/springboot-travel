package com.bronya.travel.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result<T> {
    private Integer code;       // 状态码
    private String message;     // 提示信息
    private T data;             // 返回数据


    // 成功响应（无数据）
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    // 成功响应（有数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 成功响应（自定义消息和数据）
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    // 错误响应（默认错误状态码）
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    // 错误响应（自定义状态码）
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }
}
