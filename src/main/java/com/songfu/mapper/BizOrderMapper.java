package com.songfu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.songfu.pojo.BizOrder;
import com.songfu.vo.OrderKeywordSuggestion;
import com.songfu.dto.OrderListParam;
import com.songfu.model.CustomersTotalSalesReport;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface BizOrderMapper extends BaseMapper<BizOrder> {

    /*
    * 订单信息条件分页查询
    * */
    IPage<BizOrder> list(IPage<BizOrder> page, @Param("param") OrderListParam param);


    /*
    * 根据ids查询订单
    * */
    List<BizOrder> listByIds(List<Integer> ids);

    /*
    * Excel 导出（真实单价）
    * */
    List<BizOrder> listForExcel(LocalDate beginDate, LocalDate endDate, Integer customerId);

    /*
    * 产品名称联想品名、规格1、规格2，前提是有customerId，processId与productName关键词
    * */
    List<OrderKeywordSuggestion> keywordSuggest(OrderListParam param);

    /*
     * 获取月度/年度销售总额数据
     * mode: 1-月度, 2-年度
     * startDate: 开始日期(包含)
     * endDate: 结束日期(不包含)
     * */
    List<Map<String, Object>> getTotalSalesReport(Integer mode, LocalDate startDate, LocalDate endDate);

    /*
    * 获取月度/年度分客户总产值
    * */
    List<CustomersTotalSalesReport> getCustomersTotalSalesReport(Integer mode, LocalDate startDate, LocalDate endDate);

    /*
    * 获取月度/年度各工艺产量
    * */
    List<Map<String, Object>> getProcessesTotalProduction(Integer mode, LocalDate startDate, LocalDate endDate);

    /*
    * 获取月度/年度各客户产值
    * */
    List<Map<String, Object>> getCustomersTotalProductValue(Integer mode, LocalDate startDate, LocalDate endDate);

    /*
    * 获取已被逻辑删除的订单
    * */
    List<BizOrder> listDeletedOrders();

    /*
    * 根据ids恢复已删除订单
    * */
    void restoreByIds(List<Integer> ids);

    /*
    * 获取产值(可以任意日期)
    * */
    //@MapKey("total")
    Map<String, BigDecimal> getTotalOutput(LocalDate beginDate, LocalDate endDate);

    /*
    * 获取单量
    * */
    //@MapKey("count")
    Map<String, Long> getOrderCount(LocalDate beginDate, LocalDate endDate);
}
