package com.polaris.userCenter.common;

/**
 * @Author Administrator
 * @Create 2024-02-15 17:11
 * @Version 1.0
 * ClassName ResultUtil
 * Package com.polaris.userCenter.common
 * Description
 */
public class ResultUtil {
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), "", data);
    }
    public static BaseResponse error(ResponseCode code){
        return new BaseResponse<>(code);
    }
    public static BaseResponse error(ResponseCode code, String message,String description){
         return new BaseResponse<>(code,message,description);
    }
    public static BaseResponse error(ResponseCode code,String description){
        return new BaseResponse<>(code,description);
    }
    public static BaseResponse error(int code,String message,String description){
        return new BaseResponse<>(code,message,description);
    }
}
