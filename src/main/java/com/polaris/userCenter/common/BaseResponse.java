package com.polaris.userCenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author polaris
 * @Create 2024-02-15 17:07
 * @Version 1.0
 * ClassName BaseResponse
 * Package com.polaris.userCenter.common
 * Description 通用返回类
 */
@Data
public class BaseResponse<T> implements Serializable {
    private  Integer code;
    private  String message;
    private T data;

    public BaseResponse (Integer code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse (Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public BaseResponse (Integer code, T data){
        this.code = code;
        this.data = data;
    }
}
