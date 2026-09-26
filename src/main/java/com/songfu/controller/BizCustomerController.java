package com.songfu.controller;

import com.songfu.annotation.OperationLog;
import com.songfu.annotation.RequireRole;
import com.songfu.pojo.BizCustomer;
import com.songfu.vo.Result;
import com.songfu.service.BizCustomerProcessPriceService;
import com.songfu.service.BizCustomerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/customers")
@RestController
public class BizCustomerController {

    @Autowired
    private BizCustomerService bizCustomerService;

    @Autowired
    private BizCustomerProcessPriceService bizCustomerProcessPriceService;

    /*
    * 客户信息列表查询
    * */
    @GetMapping
    @RequireRole(value = {2})
    public Result list(String customerName) {
        log.info("客户信息列表查询 {}", customerName);
        return Result.success(bizCustomerService.list(customerName));
    }

    /*
    * 获取所有客户所有工艺假单价map
    * */
    @GetMapping("/get-fake-unit-price-map")
    public Result getCustomerFakeUnitPriceMap(){
        log.info("获取所有客户所有工艺假单价map");
        return Result.success(bizCustomerProcessPriceService.getCustomerFakeUnitPriceMap());
    }

    /*
    * 根据客户id删除客户
    * */
    @DeleteMapping
    @OperationLog("根据客户id删除客户")
    @RequireRole(value = {2})
    public Result deleteByCustomerId(Integer customerId) {
        log.info("根据客户id删除客户 {}", customerId);
        bizCustomerService.deleteByCustomerId(customerId);
        return Result.success();
    }

    /*
    * 添加客户
    * */
    @PostMapping
    @OperationLog("添加客户")
    @RequireRole(value = {2})
    public Result add(@Valid @RequestBody BizCustomer customer){
        log.info("添加客户: {}", customer);
        bizCustomerService.add(customer);
        return Result.success();
    }

    /*
    * 修改客户
    * */
    @PutMapping
    @OperationLog("修改客户")
    @RequireRole(value = {2})
    public Result update(@Valid @RequestBody BizCustomer customer){
        log.info("修改客户: {}", customer);
        bizCustomerService.update(customer);
        return Result.success();
    }

    /*
    * 获取客户基本信息
    * */
    @GetMapping("/simple-list")
    public Result simpleList(){
        log.info("获取客户基本信息");
        return Result.success(bizCustomerService.simpleList());
    }

}
