package com.songfu.controller;

import com.songfu.annotation.OperationLog;
import com.songfu.annotation.RequireRole;
import com.songfu.dto.BatchAddUpdateLogParam;
import com.songfu.pojo.SysUpdateLog;
import com.songfu.service.SysUpdateLogService;
import com.songfu.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/update-logs")
public class SysUpdateLogController {

    @Autowired
    private SysUpdateLogService sysUpdateLogService;

    @GetMapping
    public Result getUpdateLogMap() {
        return Result.success(sysUpdateLogService.getUpdateLogList());
    }

    @PostMapping
    @OperationLog("批量添加更新日志")
    @RequireRole({2})
    public Result batchAdd(@RequestBody SysUpdateLog sysUpdateLog) {
        sysUpdateLogService.add(sysUpdateLog);
        return Result.success();
    }
}
