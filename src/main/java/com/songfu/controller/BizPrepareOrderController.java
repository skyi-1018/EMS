package com.songfu.controller;

import com.songfu.annotation.OperationLog;
import com.songfu.pojo.BizOrder;
import com.songfu.dto.OrderABModeParam;
import com.songfu.pojo.BizPrepareOrder;
import com.songfu.vo.Result;
import com.songfu.service.BizPrepareOrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/prepare-orders")
@RestController
public class BizPrepareOrderController {

    @Autowired
    private BizPrepareOrderService bizPrepareOrderService;

    /*
     * 预备订单信息列表查询
     * */
    @GetMapping
    public Result list() {
        log.info("预备订单信息列表查询");
        return Result.success(bizPrepareOrderService.list());
    }

    /*
    * 根据ids批量删除预备订单
    * */
    @DeleteMapping
    @OperationLog("根据ids批量删除预备订单")
    public Result deleteByIds(@RequestParam List<Integer> ids) {
        log.info("根据ids批量删除预备订单: {}", ids);
        bizPrepareOrderService.batchDeleteByIds(ids);
        return Result.success();
    }

    /*
    * 添加预备订单
    * */
    @PostMapping
    @OperationLog("添加预备订单")
    public Result add(@Valid @RequestBody BizPrepareOrder bizPrepareOrder) {
        log.info("添加预备订单: {}", bizPrepareOrder);
        bizPrepareOrderService.add(bizPrepareOrder);
        return Result.success();
    }

    /*
    * 修改预备订单
    * */
    @PutMapping
    @OperationLog("修改预备订单")
    public Result update(@Valid @RequestBody BizPrepareOrder bizPrepareOrder) {
        log.info("修改预备订单: {}", bizPrepareOrder);
        bizPrepareOrderService.update(bizPrepareOrder);
        return Result.success();
    }

    /*
    * 预备订单提交至正式订单
    * */
    @PostMapping("/submit")
    @OperationLog("预备订单提交至正式订单")
    public Result submit(@Valid @RequestBody BizOrder order) {
        log.info("预备订单提交至正式订单: {}", order);
        bizPrepareOrderService.submit(order);
        return Result.success();
    }

    /*
    * AB面模式添加预备订单
    * */
    @PostMapping("/ABMode-add")
    @OperationLog("AB面模式添加预备订单")
    public Result ABModeAdd(@RequestBody OrderABModeParam orderABMode) {
        log.info("AB面模式添加预备订单: {}", orderABMode);
        bizPrepareOrderService.ABModeAdd(orderABMode);
        return Result.success();
    }

    /*
    * 提交并打印订单
    * */
    @PostMapping("/submit-and-print")
    @OperationLog("提交并打印预备订单")
    public Result submitAndPrint(@Valid @RequestBody BizOrder order) throws Exception {
        log.info("提交并打印预备订单: {}", order);
        bizPrepareOrderService.submitAndPrint(order);
        return Result.success();
    }
}
