package com.songfu.service.impl;

import com.songfu.mapper.BizProcessMapper;
import com.songfu.pojo.BizProcess;
import com.songfu.service.BizProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BizProcessServiceImpl implements BizProcessService {

    @Autowired
    private BizProcessMapper bizProcessMapper;

    @Override
    public List<BizProcess> list() {
        return bizProcessMapper.selectList(null);
    }

    @Override
    public void update(BizProcess process) {
        bizProcessMapper.updateById(process);
    }

    @Override
    public void deleteById(Integer id) {
        bizProcessMapper.deleteById(id);
    }

    @Override
    public void add(BizProcess process) {
        bizProcessMapper.insert(process);
    }
}
