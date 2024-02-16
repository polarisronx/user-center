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
    private String description;

    public BaseResponse (Integer code, String message, T data,String description){
        this.code = code;
        this.message = message;
        this.data = data;
        this.description = description;
    }

    public BaseResponse (Integer code, String message, T data){
         this(code,message,data,"");
     }

    public BaseResponse (Integer code, String message){
        this(code, message,null,"");
    }

    public BaseResponse (Integer code, T data){
        this(code,null,data,"");
    }
    public BaseResponse (ResponseCode code){
         this(code.getCode(),code.getMessage(),null,code.getDescription());
    }
    public BaseResponse (ResponseCode code, String description){
         this(code.getCode(), code.getMessage(),null,description);
    }
    public BaseResponse (ResponseCode code, String message,String description){
        this(code.getCode(),message,null,description);
    }
}
