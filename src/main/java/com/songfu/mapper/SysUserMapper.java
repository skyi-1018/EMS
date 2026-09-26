package com.songfu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.songfu.pojo.SysUser;
import com.songfu.vo.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("select id, username, account, password, role, create_time, update_time from sys_user where account = #{account}")
    SysUser getByAccount(String account);

    /*
    * 获取用户列表
    * */
    @Select("select id, username from sys_user")
    List<UserInfo> getUserList();
}
