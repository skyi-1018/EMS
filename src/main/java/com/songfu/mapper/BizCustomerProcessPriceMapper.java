package com.songfu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.songfu.pojo.BizCustomer;
import com.songfu.pojo.BizCustomerProcessPrice;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BizCustomerProcessPriceMapper extends BaseMapper<BizCustomerProcessPrice> {
    /*
    * 根据客户id查询其所有工艺单价
    * */
    @Select("select customer_id, process_id, real_unit_price, fake_unit_price from biz_customer_process_price where customer_id = #{customerId}")
    List<BizCustomerProcessPrice> getCustomerUnitPriceById(Integer customerId);

    /*
    * 根据客户id删除其所有工艺单价
    * */
    @Delete("delete from biz_customer_process_price where customer_id = #{customerId}")
    void deleteByCustomerId(Integer customerId);

    /*
    * 新增客户单价信息
    * */
    void add(List<BizCustomerProcessPrice> unitPriceList);

    /*
    * 获取客户id：真假单价Map
    * */
    @Select("select customer_id, process_id, real_unit_price, fake_unit_price from biz_customer_process_price")
    List<BizCustomerProcessPrice> getCustomerFakeUnitPriceMap();

}
