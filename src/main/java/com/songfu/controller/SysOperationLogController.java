package com.songfu.controller;

import com.songfu.annotation.RequireRole;
import com.songfu.vo.Result;
import com.songfu.dto.LogParam;
import com.songfu.service.SysOperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/logs")
public class SysOperationLogController {

    @Autowired
    private SysOperationLogService sysOperationLogService;

    @PostMapping("/list")
    @RequireRole({2})
    public Result list(@RequestBody LogParam param) {
        log.info("查询操作日志：{}", param);
        return Result.success(sysOperationLogService.list(param));
    }
}
