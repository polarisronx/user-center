package com.polaris.usercenter.service.impl;

import com.polaris.usercenter.model.domain.User;
import com.polaris.usercenter.service.UserService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Author Administrator
 * @Create 2024-01-29 15:02
 * @Version 1.0
 * ClassName UserServiceImplTest
 * Package com.polaris.usercenter.service.impl
 * Description
 */
@SpringBootTest
class UserServiceImplTest {
    @Resource
    private UserService userService;
    @Test
    void insertUser() {
        User user = new User();

        user.setUserAccount("123");
        user.setAvatarUrl("https://www.baidu.com/s?wd=%E7%99%BE%E5%BA%A6%E7%83%AD%E6%90%9C&sa=ire_dl_gh_logo_texing&rsv_dl=igh_logo_pc");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("110");
        user.setEmail("123@321.com");
        user.setUserStatus(0);
        user.setIsDelete(0);

        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

}