package com.songfu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songfu.dto.BatchAddUpdateLogParam;
import com.songfu.mapper.SysUpdateLogMapper;
import com.songfu.pojo.SysUpdateLog;
import com.songfu.service.SysUpdateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysUpdateLogServiceImpl implements SysUpdateLogService {

    @Autowired
    private SysUpdateLogMapper sysUpdateLogMapper;

    @Override
    public List<SysUpdateLog> getUpdateLogList() {
        return sysUpdateLogMapper.selectList(new LambdaQueryWrapper<SysUpdateLog>()
                .orderByDesc(SysUpdateLog::getUpdateDate));
    }

    @Override
    public void add(SysUpdateLog sysUpdateLog) {
        sysUpdateLogMapper.insert(sysUpdateLog);
    }

}
