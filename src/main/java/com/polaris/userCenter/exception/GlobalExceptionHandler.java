package com.polaris.userCenter.exception;

import com.polaris.userCenter.common.BaseResponse;
import com.polaris.userCenter.common.ResponseCode;
import com.polaris.userCenter.common.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author Administrator
 * @Create 2024-02-16 22:04
 * @Version 1.0
 * ClassName GlobalExceptionHandler
 * Package com.polaris.userCenter.exception
 * Description
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse handleBusinessException(BusinessException e) {
        log.error("BusinessException: " + e.getMessage(), e);
        return ResultUtil.error(e.getCode(), e.getMessage(),e.getDescription());
    }
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException: " + e.getMessage(), e);
        return ResultUtil.error(ResponseCode.SYSTEM_ERROR,e.getMessage(),"系统异常");
    }
}


