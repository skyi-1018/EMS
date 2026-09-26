package com.songfu.service;

import com.songfu.vo.PageResult;
import com.songfu.dto.LogParam;
import com.songfu.pojo.SysOperationLog;

public interface SysOperationLogService {

    /*
    * 记录日志
    * */
    void save(SysOperationLog log);

    /*
    * 获取日志
    * */
    PageResult<SysOperationLog> list(LogParam param);
}
