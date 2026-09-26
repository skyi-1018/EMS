package com.songfu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.songfu.pojo.BizPrepareOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BizPrepareOrderMapper extends BaseMapper<BizPrepareOrder> {
    /*
    * 预备订单信息列表查询
    * */
    List<BizPrepareOrder> list();
}
