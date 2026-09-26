package com.songfu.service.impl;

import com.songfu.mapper.BizOrderMapper;
import com.songfu.pojo.BizCustomer;
import com.songfu.service.BizCustomerService;
import com.songfu.service.BizReportService;
import com.songfu.utils.CommonUtil;
import com.songfu.model.CustomersTotalSalesReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BizReportServiceImpl implements BizReportService {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private BizOrderMapper orderMapper;

    @Autowired
    private BizCustomerService customerService;

    /*
    * 需要给前端返回的数据格式
    * [
    *   ['product', '分类1', '分类2', '分类3', ...],
    *   ['X轴1', '分类1值', '分类2值', '分类3值', ...],
    *   ['X轴2', '分类1值', '分类2值', '分类3值', ...],
    *   ...
    * ]
    * */

    @Override
    public List<List<Object>> getTotalSales(Integer mode, LocalDate date) {
        // 1.处理date，获取开始日期与结束日期
        LocalDate[] dateRange = getStartDateAndEndDate(mode, date);
        LocalDate startDate = dateRange[0];
        LocalDate endDate = dateRange[1];

        // 2.数据库查询
        List<Map<String, Object>> result = orderMapper.getTotalSalesReport(mode, startDate, endDate);
        Map<String, Double> dateSaleMap = result.stream().collect(Collectors.toMap(
                row -> row.get("order_date").toString(),
                row -> Double.parseDouble(row.get("total_amount").toString())
        ));

        // 3.获取日期
        List<String> dateList = commonUtil.getAllDateList(mode, startDate, endDate);

        // 4.组装数据
        List<List<Object>> data = new ArrayList<>();
        data.add(List.of("product", "总产值"));
        dateList.forEach(d -> {
            data.add(List.of(d, dateSaleMap.get(d) != null ? dateSaleMap.get(d) : 0));
        });
        return data;
    }

    @Override
    public List<List<Object>> getCustomersTotalSales(Integer mode, LocalDate date) {
        // 1.处理date，获取开始日期与结束日期
        LocalDate[] dateRange = getStartDateAndEndDate(mode, date);
        LocalDate startDate = dateRange[0];
        LocalDate endDate = dateRange[1];

        // 2.数据库查询
        List<CustomersTotalSalesReport> result = orderMapper.getCustomersTotalSalesReport(mode, startDate, endDate);
        Map<String, Map<Object, Object>> dateCustomersSaleMap = new HashMap<>();
        result.forEach(row -> {
            dateCustomersSaleMap.computeIfAbsent(row.getOrderDate(), k -> new HashMap<>()).put((Integer) row.getCustomerId(), row.getTotalAmount().doubleValue());
        });

        // 3.获取日期
        List<String> dateList = commonUtil.getAllDateList(mode, startDate, endDate);

        // 4.获取客户列表
        List<BizCustomer> customerList = customerService.simpleList();

        // 5.组装数据
        List<List<Object>> data = new ArrayList<>();
        List<Object> header = new ArrayList<>(List.of("product"));
        customerList.forEach(customer -> {
            header.add(customer.getName());
        });
        data.add(header);
        dateList.forEach(d -> {
            List<Object> row = new ArrayList<>(List.of(d));
            customerList.forEach(customer -> {
                if (dateCustomersSaleMap.get(d) != null) {
                    row.add(dateCustomersSaleMap.get(d).get(customer.getId()) != null ? dateCustomersSaleMap.get(d).get(customer.getId()) : 0);
                } else {
                    row.add(0);
                }
            });
            data.add(row);
        });
        return data;
    }

    @Override
    public List<List<Object>> getProcessesTotalProduction(Integer mode, LocalDate date) {
        // 1.处理date，获取开始日期与结束日期
        LocalDate[] dateRange = getStartDateAndEndDate(mode, date);
        LocalDate startDate = dateRange[0];
        LocalDate endDate = dateRange[1];

        // 2.数据库查询
        List<Map<String, Object>> result = orderMapper.getProcessesTotalProduction(mode, startDate, endDate);

        // 3.组装数据
        List<List<Object>> data = new ArrayList<>();
        data.add(List.of("product", "工艺产量"));
        result.forEach(row -> {
            data.add(List.of(row.get("process_name"), row.get("total_production")));
        });
        return data;
    }

    @Override
    public List<List<Object>> getCustomersTotalProductValue(Integer mode, LocalDate date) {
        // 1.处理date，获取开始日期与结束日期
        LocalDate[] dateRange = getStartDateAndEndDate(mode, date);
        LocalDate startDate = dateRange[0];
        LocalDate endDate = dateRange[1];

        // 2.数据库查询
        List<Map<String, Object>> result = orderMapper.getCustomersTotalProductValue(mode, startDate, endDate);

        // 3.组装数据
        List<List<Object>> data = new ArrayList<>();
        data.add(List.of("product", "客户总产值"));
        result.forEach(row -> {
            data.add(List.of(row.get("customer_name"), row.get("total_amount")));
        });
        return data;
    }


    /*
    * 数据格式
    * {
    *   monthOutput: {value: 128650.5, tip: '较上月 +12.5%'},
        todayOutput: {value: 8086.24, tip: '较昨日 -8.5%'},
        monthOrderCount: {value: 305, tip: '较上月 +1.5%'},
        todayOrderCount: {value: 12, tip: '较昨日 +3.5%'},
      }
    * */
    @Override
    public Map<String, Map<String, Object>> getStatMap() {
        Map<String, Map<String, Object>> statMap = new HashMap<>();
        LocalDate now = LocalDate.now();

        LocalDate firstDayOfMonth = now.withDayOfMonth(1); // 当月第一天
        LocalDate lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1); // 当月最后一天

        LocalDate firstDayOfLastMonth = firstDayOfMonth.minusMonths(1); // 上月第一天
        LocalDate lastDayOfLastMonth = firstDayOfLastMonth.plusMonths(1).minusDays(1); // 上月最后一天

        // 1.获取当月总产值数据
        Map<String, BigDecimal> monthOutput = orderMapper.getTotalOutput(firstDayOfMonth, lastDayOfMonth);
        Map<String, BigDecimal> lastMonthOutput = orderMapper.getTotalOutput(firstDayOfLastMonth, lastDayOfLastMonth);
        Double currMonthOut = safeDouble(monthOutput, "output");
        Double lastMonthOut = safeDouble(lastMonthOutput, "output");
        String tip1 = "较上月 " + calcPercentTip(currMonthOut, lastMonthOut);
        statMap.put("monthOutput", new HashMap<>(Map.of("value", currMonthOut, "tip", tip1)));

        // 2.获取今日总产值数据
        Map<String, BigDecimal> todayOutput = orderMapper.getTotalOutput(now, now);
        LocalDate lastDay = now.minusDays(1);
        Map<String, BigDecimal> lastDayOutput = orderMapper.getTotalOutput(lastDay, lastDay);
        Double todayOut = safeDouble(todayOutput, "output");
        Double lastDayOut = safeDouble(lastDayOutput, "output");
        String tip2 = "较昨日 " + calcPercentTip(todayOut, lastDayOut);
        statMap.put("todayOutput", new HashMap<>(Map.of("value", todayOut, "tip", tip2)));

        // 3.获取当月订单数量数据
        Map<String, Long> monthOrderCount = orderMapper.getOrderCount(firstDayOfMonth, lastDayOfMonth);
        Map<String, Long> lastMonthOrderCount = orderMapper.getOrderCount(firstDayOfLastMonth, lastDayOfLastMonth);
        Integer currMonthOrderCnt = safeInt(monthOrderCount, "count");
        Integer lastMonthOrderCnt = safeInt(lastMonthOrderCount, "count");
        String tip3 = "较上月 " + calcPercentTip(currMonthOrderCnt, lastMonthOrderCnt);
        statMap.put("monthOrderCount", new HashMap<>(Map.of("value", currMonthOrderCnt, "tip", tip3)));

        // 4.获取今日订单数量数据
        Map<String, Long> todayOrderCount = orderMapper.getOrderCount(now, now);
        Map<String, Long> lastDayOrderCount = orderMapper.getOrderCount(lastDay, lastDay);
        Integer todayOrderCnt = safeInt(todayOrderCount, "count");
        Integer lastDayOrderCnt = safeInt(lastDayOrderCount, "count");
        String tip4 = "较昨日 " + calcPercentTip(todayOrderCnt, lastDayOrderCnt);
        statMap.put("todayOrderCount", Map.of("value", todayOrderCnt, "tip", tip4));

        return statMap;
    }

    /*
    * 根据mode与date获取开始日期与结束日期
    * */
    private LocalDate[] getStartDateAndEndDate(Integer mode, LocalDate date) {
        LocalDate startDate;
        LocalDate endDate;
        if (mode == 1) {
            startDate = date.withDayOfMonth(1); // 获取当月第一天
            endDate = startDate.plusMonths(1); // 获取下个月第一天
        } else if (mode == 2) {
            startDate = date.withDayOfYear(1); // 获取当年第一天
            endDate = startDate.plusYears(1);
        } else {
            throw new IllegalArgumentException("不支持的模式");
        }
        return new LocalDate[]{startDate, endDate};
    }

    private Double safeDouble(Map<String, BigDecimal> map, String key) {
        if (map == null || map.get(key) == null) {
            return 0.0;
        }
        return map.get(key).doubleValue();
    }

    private Integer safeInt(Map<String, Long> map, String key) {
        if (map == null || map.get(key) == null) {
            return 0;
        }
        return map.get(key).intValue();
    }

    private String calcPercentTip(double current, double pre) {
        if (pre == 0) {
            return "-";
        }
        double percent = (current - pre) / pre * 100;
        if (percent >= 0) {
            return String.format("+%.1f%%", percent);
        } else {
            return String.format("%.1f%%", percent);
        }
    }
}
