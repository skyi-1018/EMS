package com.songfu.service;

import com.songfu.pojo.BizCustomer;

import java.util.List;

public interface BizCustomerService {
    /*
    * 客户信息列表查询
    * */
    List<BizCustomer> list(String customerName);

    /*
    * 根据id删除客户
    * */
    void deleteByCustomerId(Integer customerId);

    /*
    * 新增客户
    * */
    void add(BizCustomer customer);

    /*
    * 更新客户
    * */
    void update(BizCustomer customer);

    /*
    * 获取客户基本信息
    * */
    List<BizCustomer> simpleList();
}
