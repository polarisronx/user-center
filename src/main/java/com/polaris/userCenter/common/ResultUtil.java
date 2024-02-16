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
        return new BaseResponse<>(0, "", data);
    }
    public static <T> BaseResponse<T> error(){
        return new BaseResponse<>(-1, "", null);
    }
}
