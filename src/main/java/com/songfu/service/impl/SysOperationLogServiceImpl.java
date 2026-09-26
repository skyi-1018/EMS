package com.songfu.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.songfu.mapper.SysOperationLogMapper;
import com.songfu.vo.PageResult;
import com.songfu.dto.LogParam;
import com.songfu.pojo.SysOperationLog;
import com.songfu.service.SysOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysOperationLogServiceImpl implements SysOperationLogService {

    @Autowired
    private SysOperationLogMapper sysOperationLogMapper;


    @Override
    @Async
    public void save(SysOperationLog log) {
        sysOperationLogMapper.insert(log);
    }

    @Override
    public PageResult<SysOperationLog> list(LogParam param) {
        //1.设置分页参数
        Page<SysOperationLog> page = new Page<>(param.getPageNum(), param.getPageSize());

        // 2.执行查询
        sysOperationLogMapper.list(page, param);

        // 3.解析结果并返回
        return new PageResult<>(page.getTotal(), page.getRecords());
    }
}
