package com.polaris.usercenter.service;

import com.polaris.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author polaris
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-01-29 15:44:13
 *
*/
public interface UserService extends IService<User> {
    long userRegister(String userAccount, String userPassword,String checkPassword);
    User Userlogin(String userAccount, String userPassword, HttpServletRequest request);
    User filterUserSafetyInfo (User originalUser);
    public boolean isAdmin(HttpServletRequest request);
}
