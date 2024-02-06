package com.polaris.usercenter.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.polaris.usercenter.mapper.UserMapper;
import com.polaris.usercenter.model.domain.User;
import com.polaris.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.polaris.usercenter.constant.UserContant.*;


/**
* @author polaris
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-01-29 15:44:13
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {


    @Override
    public long userRegister (String userAccount, String userPassword, String checkPassword){
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            log.info("用户注册信息不能为空");
            return -1;
        }
        if (userAccount.length()< 4){
            log.info("用户账号长度不能少于4位");
            return -1;
        }
        if (userPassword.length()< 8){
            log.info("用户密码长度不能少于8位");
            return -1;
        }
        if (!userPassword.equals(checkPassword)){
            log.info("两次密码输入不一致");
            return -1;
        }
        Matcher matcher = Pattern.compile("[ _`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\\n|\\r|\\t").matcher(userAccount);
        if (matcher.find()){
            log.info("用户名包含特殊字符");
            return -1;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        long count = this.count(queryWrapper);
        if (count>0){
            log.info("用户名已存在");
            return -1;
        }
        User user = new User();
        user.setUserAccount(userAccount);
        // 对密码进行加密处理
        String encodePassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        user.setUserPassword(encodePassword);
        this.save(user);
        return user.getId();
    }

    @Override
    public User Userlogin (String userAccount, String userPassword, HttpServletRequest request){
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            log.info("用户注册信息不能为空");
            return null;
        }
        if (userAccount.length()< 4){
            log.info("用户账号长度不能少于4位");
            return null;
        }
        if (userPassword.length()< 8){
            log.info("用户密码长度不能少于8位");
            return null;
        }
        Matcher matcher = Pattern.compile("[ _`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\\n|\\r|\\t").matcher(userAccount);
        if (matcher.find()){
            log.info("用户名包含特殊字符");
            return null;
        }
        // 密码加密处理
        String encodePassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在,并校验密码是否正确
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        queryWrapper.eq("user_password",encodePassword);
        User user = baseMapper.selectOne(queryWrapper);
        if (user==null){
            log.info("User failed to login:The account can not match the password,please check again!");
            return null;
        }
        // 用户信息脱敏
        User secureUser = filterUserSafetyInfo(user);
        // 记录用户登录状态
        request.getSession().setAttribute(USER_LOGIN_STATUS,secureUser);
        return secureUser;


    }
    public User filterUserSafetyInfo(User originalUser){
        if (originalUser==null){
            return null;
        }
        User secureUser = new User();
        secureUser.setId(secureUser.getId());
        secureUser.setUserAccount(originalUser.getUserAccount());
        secureUser.setNickname(originalUser.getNickname());
        secureUser.setAvatarUrl(originalUser.getAvatarUrl());
        secureUser.setGender(originalUser.getGender());
        secureUser.setPhone(originalUser.getPhone());
        secureUser.setEmail(originalUser.getEmail());
        secureUser.setUserStatus(originalUser.getUserStatus());
        secureUser.setCreateTime(originalUser.getCreateTime());
        secureUser.setUserRole(originalUser.getUserRole());
        return  secureUser;
    }
    // 统一 鉴权 方法
    public boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User user = (User)userObj;
        if (user==null){
            return false;
        }
        return user.getUserRole()==ADMIN_ROLE;
    }
}




