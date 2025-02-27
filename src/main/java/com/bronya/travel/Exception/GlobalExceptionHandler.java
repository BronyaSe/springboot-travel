package com.bronya.travel.Exception;

import com.bronya.travel.Entity.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result GlobalException(Exception e) {
        e.printStackTrace();
        return Result.error(StringUtils.hasLength(e.getMessage())? e.getMessage() : "操作失败");
    }
}
