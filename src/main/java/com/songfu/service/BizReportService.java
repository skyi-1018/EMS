package com.songfu.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface BizReportService {

    /*
    * 获取月度/年度总产值
    * */
    public List<List<Object>> getTotalSales(Integer mode, LocalDate date);

    /*
    * 获取月度/年度分客户总产值
    * */
    public List<List<Object>> getCustomersTotalSales(Integer mode, LocalDate date);

    /*
    * 获取月度/年度各工艺产量
    * */
    public List<List<Object>> getProcessesTotalProduction(Integer mode, LocalDate date);

    /*
    * 获取月度/年度各客户产值
    * */
    public List<List<Object>> getCustomersTotalProductValue(Integer mode, LocalDate date);

    /*
    * 获取首页数据Map
    * */
    public Map<String, Map<String, Object>> getStatMap();
}
