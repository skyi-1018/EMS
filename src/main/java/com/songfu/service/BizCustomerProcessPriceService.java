package com.songfu.service;

import com.songfu.pojo.BizCustomerProcessPrice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BizCustomerProcessPriceService {
    /*
    * 根据客户id查询其所有工艺单价
    * */
    Map<Integer, Map<String, BigDecimal>> getCustomerUnitPriceById(Integer customerId);

    /*
    * 获取所有客户所有工艺真假单价map
    * */
    Map<Integer, Map<Integer, Map<String, BigDecimal>>> getCustomerFakeUnitPriceMap();

    /*
    * 添加客户工艺单价Map
    * */
    void addCustomerUnitPriceMap(Integer customerId, Map<Integer, Map<String, BigDecimal>> customerUnitPriceMap);

}
