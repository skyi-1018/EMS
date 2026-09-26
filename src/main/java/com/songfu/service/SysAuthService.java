package com.songfu.service;


import com.songfu.pojo.SysUser;
import com.songfu.vo.UserInfo;

import java.util.List;
import java.util.Map;

public interface SysAuthService {

    /*
    * 注册用户
    * */
    void register(SysUser user);


    /*
    * 用户登录
    * */
    Map<String, Object> login(SysUser user);

    /*
    * 获取当前请求用户信息，根据token获取
    * */
    UserInfo me();

    /*
    * 获取用户列表
    * */
    List<UserInfo> getUserList();
}
