package com.songfu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.songfu.dto.LogParam;
import com.songfu.pojo.SysOperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.jena.base.Sys;

import java.util.List;

@Mapper
public interface SysOperationLogMapper extends BaseMapper<SysOperationLog> {
    /*
    * 查询日志
    * */
    IPage<SysOperationLog> list(IPage<SysOperationLog> page, @Param("param") LogParam param);
}
