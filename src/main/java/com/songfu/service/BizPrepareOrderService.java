package com.songfu.service;


import com.songfu.pojo.BizOrder;
import com.songfu.dto.OrderABModeParam;
import com.songfu.pojo.BizPrepareOrder;

import java.util.List;

public interface BizPrepareOrderService {
    /*
    * 预备订单信息列表查询
    * */
    List<BizPrepareOrder> list();

    /*
    * 根据ids批量删除预备订单
    * */
    void batchDeleteByIds(List<Integer> ids);

    /*
    * 添加预备订单
    * */
    void add(BizPrepareOrder bizPrepareOrder);

    /*
    * 修改预备订单
    * */
    void update(BizPrepareOrder bizPrepareOrder);

    /*
    * 提交预备订单至正式订单
    * */
    Integer submit(BizOrder bizOrder);

    /*
     * AB面模式添加预备订单
     * */
    void ABModeAdd(OrderABModeParam orderABMode);

    /*
    * 提交并打印预备订单
    * */
    void submitAndPrint(BizOrder bizOrder) throws Exception;
}
