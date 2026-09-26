package com.songfu.service;


import com.songfu.dto.BatchAddUpdateLogParam;
import com.songfu.pojo.SysUpdateLog;

import java.util.List;
import java.util.Map;

public interface SysUpdateLogService {

    /*
    * 获取更新日志列表
    * */
    List<SysUpdateLog> getUpdateLogList();

    /*
    * 添加更新日志
    * */
    void add(SysUpdateLog sysUpdateLog);
}
