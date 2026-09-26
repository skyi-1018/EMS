package com.songfu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.songfu.pojo.BizCustomer;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface BizCustomerMapper extends BaseMapper<BizCustomer> {
    /*
    * 客户信息列表查询
    * */
    List<BizCustomer> list(String customerName);
}
