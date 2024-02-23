package com.polaris.userCenter.service;

import com.polaris.userCenter.model.UserRequest.RegisterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @Author polaris
 * @Create 2024-02-22 15:59
 * @Version 1.0
 * ClassName PythonService
 * Package com.polaris.userCenter.service
 * Description 运用feign远程调用的python服务
 * Copyright(c) 2024 by polaris.
 */
@FeignClient(value = "service-django")
@Component
public interface PythonService {
    @PostMapping ("user/register")
    HashMap<String,Object> userRegister(@RequestBody RegisterRequest registerRequest);


}
