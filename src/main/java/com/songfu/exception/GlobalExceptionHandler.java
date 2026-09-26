package com.songfu.exception;

import com.songfu.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/*
* 全局异常捕获
* */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
    * 捕获参数校验异常
    * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        FieldError firstError = fieldErrors.get(0);
        String msg = firstError.getDefaultMessage();
        return Result.error(msg);
    }

    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleAllException(Exception e) {
        // 区分受检异常，打印完整堆栈，方便排查打印、文件、命令执行报错
        log.error("系统未知异常", e);
        return Result.error("系统内部异常，请联系管理员");
    }
}
