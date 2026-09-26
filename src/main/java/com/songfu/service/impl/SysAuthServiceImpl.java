package com.songfu.service.impl;

import com.songfu.common.UserContext;
import com.songfu.mapper.SysUserMapper;
import com.songfu.pojo.SysUser;
import com.songfu.vo.UserInfo;
import com.songfu.service.SysAuthService;
import com.songfu.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysAuthServiceImpl implements SysAuthService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private JWTUtil jwtUtil;

    @Value("${my.admin-password}")
    private String adminPassword;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void register(SysUser user) {
        if (!user.getAdminPassword().equals(adminPassword)) throw new RuntimeException("管理员密码错误");
        if (sysUserMapper.getByAccount(user.getAccount()) != null) {
            throw new RuntimeException("账号已存在");
        }
        user.setRole(1);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        sysUserMapper.insert(user);
    }

    public Map<String, Object> login(SysUser user) {
        String account = user.getAccount();
        String password = user.getPassword();

        SysUser dbUser = sysUserMapper.getByAccount(account);
        if (dbUser == null || !encoder.matches(password, dbUser.getPassword())) {
            throw new RuntimeException("账号或密码错误");
        }

        String token = jwtUtil.generateToken(dbUser.getId(), dbUser.getUsername(), dbUser.getRole());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        UserInfo userInfo = new UserInfo(dbUser.getId(), dbUser.getUsername(), dbUser.getAccount(), dbUser.getRole());
        data.put("userInfo", userInfo);

        return data;
    }

    @Override
    public UserInfo me() {
        SysUser user = sysUserMapper.selectById(UserContext.getUserId());
        return new UserInfo(user.getId(), user.getUsername(), user.getAccount(), user.getRole());
    }

    @Override
    public List<UserInfo> getUserList() {
        return sysUserMapper.getUserList();
    }
}
