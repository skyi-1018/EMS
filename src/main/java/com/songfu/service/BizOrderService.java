package com.songfu.service;

import com.songfu.dto.OrderABModeParam;
import com.songfu.dto.OrderListParam;
import com.songfu.model.ExcelExportItem;
import com.songfu.vo.OrderKeywordSuggestion;
import com.songfu.pojo.*;
import com.songfu.vo.PageResult;

import java.time.YearMonth;
import java.util.List;

public interface BizOrderService {

    /*
     * 订单信息条件分页查询
     * */
    PageResult<BizOrder> list(OrderListParam param);

    /*
    * 根据id删除订单
    * */
    void deleteById(Integer id);

    /*
    * 添加订单
    * */
    Integer add(BizOrder order);

    /*
    * 修改订单
    * */
    void update(BizOrder order);

    /*
    * Excel导出
    * */
    List<ExcelExportItem<BizOrder>> excelExport(Integer mode, YearMonth month, Integer customerId);

    /*
    * 产品名称联想品名、规格1、规格2，前提是有customerId，processId与productName关键词
    * */
    List<OrderKeywordSuggestion> keywordSuggest(OrderListParam param);

    /*
    * AB面模式添加订单
    * */
    List<Integer> ABModeAdd(OrderABModeParam orderABMode);

    /*
    * AB面提交并打印
    * */
    void ABModeSubmitAndPrint(OrderABModeParam orderABMode) throws Exception;

    /*
    * 普通提交并打印
    * */
    void submitAndPrint(BizOrder order) throws Exception;

    /*
    * 获取已被逻辑删除的订单列表
    * */
    List<BizOrder> listDeletedOrders();

    /*
    * 根据ids恢复已删除订单
    * */
    void restoreByIds(List<Integer> ids);
}
