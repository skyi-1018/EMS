package com.songfu.controller;

import com.songfu.annotation.RequireRole;
import com.songfu.vo.Result;
import com.songfu.service.BizReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reports")
public class BizReportController {

    @Autowired
    private BizReportService bizReportService;


    /*
    * 获取月度/年度销售总额
    * mode: 1-月度 2-年度
    * date: yyyy-MM-dd
    * */
    @RequireRole({2})
    @GetMapping("/total-sales")
    public Result getTotalSales(@RequestParam Integer mode, @RequestParam LocalDate date) {
        log.info("获取月度/年度销售总额，mode: {}", mode);
        List<List<Object>> result = bizReportService.getTotalSales(mode, date);
        return Result.success(result);
    }

    /*
    * 获取月度/年度客户总销售额
    * mode: 1-月度 2-年度
    * date: yyyy-MM-dd
    * */
    @RequireRole({2})
    @GetMapping("/customers-total-sales")
    public Result getCustomersTotalSales(@RequestParam Integer mode, @RequestParam LocalDate date) {
        log.info("获取月度/年度客户总销售额，mode: {}", mode);
        List<List<Object>> result = bizReportService.getCustomersTotalSales(mode, date);
        return Result.success(result);
    }

    /*
    * 获取月度/年度各工艺产量
    * mode: 1-月度 2-年度
    * date: yyyy-MM-dd
    * */
    @RequireRole({2})
    @GetMapping("/processes-total-production")
    public Result getProcessesTotalProduction(@RequestParam Integer mode, @RequestParam LocalDate date) {
        log.info("获取月度/年度各工艺产量，mode: {}", mode);
        List<List<Object>> result = bizReportService.getProcessesTotalProduction(mode, date);
        return Result.success(result);
    }

    /*
    * 获取月度/年度各客户总产值
    * */
    @RequireRole({2})
    @GetMapping("/customers-total-product-value")
    public Result getCustomersTotalProductValue(@RequestParam Integer mode, @RequestParam LocalDate date) {
        log.info("获取月度/年度各客户总产值，mode: {}", mode);
        List<List<Object>> result = bizReportService.getCustomersTotalProductValue(mode, date);
        return Result.success(result);
    }

    /*
    * 获取首页数据Map
    * */
    @GetMapping("/get-stat-map")
    public Result getStatMap() {
        log.info("获取首页数据Map");
        return Result.success(bizReportService.getStatMap());
    }
}
