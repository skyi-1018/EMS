package com.songfu.service;

import com.songfu.model.ExcelExportItem;
import com.songfu.vo.PageResult;
import com.songfu.dto.LogParam;
import com.songfu.pojo.SysOperationLog;

import java.time.LocalDate;

public interface SysOperationLogService {

    /*
    * 记录日志
    * */
    void save(SysOperationLog log);

    /*
    * 获取日志
    * */
    PageResult<SysOperationLog> list(LogParam param);

    /*
    * Excel导出
    * */
    ExcelExportItem<SysOperationLog> excelExport(LocalDate startDate, LocalDate endDate);
}
