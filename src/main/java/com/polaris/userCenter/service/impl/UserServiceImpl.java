package com.polaris.userCenter.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.polaris.userCenter.common.ResponseCode;
import com.polaris.userCenter.exception.BusinessException;
import com.polaris.userCenter.mapper.UserMapper;
import com.polaris.userCenter.model.domain.User;
import com.polaris.userCenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.polaris.userCenter.constant.UserConstant.*;


/**
* @author polaris
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-01-29 15:44:13
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    /*
     * @author polaris
     * @description 用户注册
     **/
    @Override
    public long userRegister (String userAccount, String userPassword, String checkPassword,String authCode){
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,authCode)){
            throw new BusinessException(ResponseCode.NULL_ERROR,"用户注册信息不能为空");
        }
        if (userAccount.length()< 4){
            throw new BusinessException(ResponseCode.PARAMS_ERROR,"用户账号长度不能少于4位");
        }
        if (userPassword.length()< 8){
            throw new BusinessException(ResponseCode.PARAMS_ERROR,"用户密码长度不能少于8位");
        }
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ResponseCode.PARAMS_ERROR,"两次密码输入不一致");
        }
        Matcher matcher = Pattern.compile("[ _`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\\n|\\r|\\t").matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ResponseCode.PARAMS_ERROR,"用户名包含特殊字符");
        }
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        long count = this.count(queryWrapper);
        if (count>0){
            throw new BusinessException(ResponseCode.PARAMS_ERROR,"用户名已存在");
        }
        // 查询授权码是否重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("auth_code",authCode);
        count = this.count(queryWrapper);
        if (count>0){
            throw new BusinessException(ResponseCode.PARAMS_ERROR,"授权码已被使用");
        }
        User user = new User();
        user.setUserAccount(userAccount);
        // 对密码进行加密处理
        String encodePassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        user.setUserPassword(encodePassword);
        user.setAuthCode(authCode);
        boolean result = this.save(user);
        if (!result){
            throw new BusinessException(ResponseCode.PARAMS_ERROR,"注册失败");
        }
        return user.getId();
    }

    /*
     * @author polaris
     * @description 用户登录
     **/
    @Override
    public User UserLogin (String userAccount, String userPassword, HttpServletRequest request){
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ResponseCode.NULL_ERROR,"用户登录信息不能为空");
        }
        if (userAccount.length()< 4){
            throw new BusinessException(ResponseCode.PARAMS_ERROR,"用户账号长度不能少于4位");
        }
        if (userPassword.length()< 8){
            throw new BusinessException(ResponseCode.PARAMS_ERROR,"用户密码长度不能少于8位");
        }
        Matcher matcher = Pattern.compile("[ _`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\\n|\\r|\\t").matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ResponseCode.PARAMS_ERROR,"用户名包含特殊字符");
        }
        // 密码加密处理
        String encodePassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在,并校验密码是否正确
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        queryWrapper.eq("user_password",encodePassword);
        User user = baseMapper.selectOne(queryWrapper);
        if (user==null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR,"用户登录失败，请检查账号和密码！");
        }
        // 用户信息脱敏
        User secureUser = filterUserSafetyInfo(user);
        // 记录用户登录状态
        request.getSession().setAttribute(USER_LOGIN_STATUS,secureUser);
        return secureUser;


    }

    /*
     * @author polaris
     * @description 用户信息脱敏
     **/
    public User filterUserSafetyInfo(User originalUser){
        if (originalUser==null){
            throw new BusinessException(ResponseCode.NULL_ERROR,"用户不存在");
        }
        User secureUser = new User();
        secureUser.setId(originalUser.getId());
        secureUser.setUserAccount(originalUser.getUserAccount());
        secureUser.setNickname(originalUser.getNickname());
        secureUser.setAvatarUrl(originalUser.getAvatarUrl());
        secureUser.setGender(originalUser.getGender());
        secureUser.setPhone(originalUser.getPhone());
        secureUser.setEmail(originalUser.getEmail());
        secureUser.setUserStatus(originalUser.getUserStatus());
        secureUser.setCreateTime(originalUser.getCreateTime());
        secureUser.setAuthCode(originalUser.getAuthCode());
        secureUser.setUserRole(originalUser.getUserRole());
        return  secureUser;
    }
    // 统一 鉴权 方法
    public boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User user = (User)userObj;
        if (user==null){
            throw new BusinessException(ResponseCode.NOT_LOGIN_ERROR);
        }
        return user.getUserRole()==ADMIN_ROLE;
    }

    /*
     * @author polaris
     * @create 2024/2/15
     * @description 用户注销
     **/
    @Override
    public int UserLogout (HttpServletRequest request){
        if (request==null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        request.getSession().removeAttribute(USER_LOGIN_STATUS);
        return 1;
    }
}




