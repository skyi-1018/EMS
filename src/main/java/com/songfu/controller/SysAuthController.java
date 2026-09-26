package com.songfu.controller;

import com.songfu.annotation.OperationLog;
import com.songfu.annotation.RequireRole;
import com.songfu.common.UserContext;
import com.songfu.vo.Result;
import com.songfu.pojo.SysUser;
import com.songfu.service.SysAuthService;
import com.songfu.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auths")
public class SysAuthController {

    @Autowired
    private SysAuthService sysAuthService;
    @Autowired
    private JWTUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    @OperationLog("用户登录")
    public Result login(@RequestBody SysUser user) {
        log.info("登录：{}", user);
        return Result.success(sysAuthService.login(user));
    }

    @GetMapping("/me")
    public Result me() {
        log.info("获取当前用户信息 {}", UserContext.getUserId());
        return Result.success(sysAuthService.me());
    }

    @PostMapping("/register")
    @OperationLog("用户注册")
    public Result register(@RequestBody SysUser user) {
        log.info("用户注册：{}", user);
        sysAuthService.register(user);
        return Result.success();
    }

    /*
    * 获取用户列表，不包含敏感信息
    * */
    @GetMapping("/user-list")
    @RequireRole({2})
    public Result getUserList() {
        log.info("获取用户列表");
        return Result.success(sysAuthService.getUserList());
    }
}
