package com.songfu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.songfu.pojo.SysUpdateLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUpdateLogMapper extends BaseMapper<SysUpdateLog> {

}
