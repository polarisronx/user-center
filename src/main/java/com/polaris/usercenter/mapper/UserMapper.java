package com.polaris.usercenter.mapper;

import com.polaris.usercenter.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2024-01-29 15:44:13
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




