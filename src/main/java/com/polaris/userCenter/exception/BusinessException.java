package com.polaris.userCenter.exception;

import com.polaris.userCenter.common.ResponseCode;
import lombok.Getter;

/**
 * @Author Administrator
 * @Create 2024-02-16 18:41
 * @Version 1.0
 * ClassName BusinessException
 * Package com.polaris.userCenter.exception
 * Description
 */
@Getter
public class BusinessException extends RuntimeException{
    private final int code;
    private final String description;

    public BusinessException (int code, String description){
        this.code = code;
        this.description = description;
    }
    public BusinessException (ResponseCode code){
        this(code.getCode(), code.getDescription());
    }
    public BusinessException (ResponseCode code, String description){
        this(code.getCode(),description);
    }
}
