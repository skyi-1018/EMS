package com.songfu.controller;

import com.songfu.annotation.OperationLog;
import com.songfu.annotation.RequireRole;
import com.songfu.dto.OrderABModeParam;
import com.songfu.dto.OrderListParam;
import com.songfu.model.ExcelExportItem;
import com.songfu.pojo.*;
import com.songfu.service.BizOrderService;
import com.songfu.utils.ExcelExportUtil;
import com.songfu.vo.Result;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@RequestMapping("/orders")
@RestController
public class BizOrderController {

    @Autowired
    private BizOrderService bizOrderService;

    @Autowired
    private ExcelExportUtil excelExportUtil;

    /*
     * 订单信息条件分页查询
     * */
    @PostMapping("/list")
    public Result list(@RequestBody OrderListParam param){
        log.info("订单信息条件分页查询: {}", param);
        return Result.success(bizOrderService.list(param));
    }

    /*
    * 根据id删除订单
    * */
    @OperationLog("删除订单")
    @DeleteMapping
    public Result deleteById(Integer id){
        log.info("根据id删除订单: {}", id);
        bizOrderService.deleteById(id);
        return Result.success();
    }

    /*
    * 添加订单
    * */
    @OperationLog("添加订单")
    @PostMapping
    public Result add(@Valid @RequestBody BizOrder order){
        log.info("添加订单: {}", order);
        bizOrderService.add(order);
        return Result.success();
    }
    /*
    * 修改订单
    * */
    @OperationLog("修改订单")
    @PutMapping
    public Result update(@Valid @RequestBody BizOrder order){
        log.info("修改订单: {}", order);
        bizOrderService.update(order);
        return Result.success();
    }

    /*
    * Excel导出
    * */
    @GetMapping("/export")
    @RequireRole({2})
    public void excelExport(@RequestParam Integer mode,
                              @RequestParam YearMonth month,
                              @RequestParam(required = false) Integer customerId,
                              HttpServletResponse response) throws IOException {
        log.info("Excel导出: mode = {}, month = {}, customerId = {}", mode, month, customerId);
        // 1.调用service拿到组装好的所有excel元数据
        List<ExcelExportItem> itemList = bizOrderService.excelExport(mode, month, customerId);
        if(itemList.isEmpty()){
            throw new RuntimeException("没有可导出的数据");
        }

        byte[] downloadBytes;
        String downloadFileName;
        String contentType;

        // 2.根据item数量判断输出格式
        if (itemList.size() == 1) {
            ExcelExportItem item = itemList.get(0);
            downloadBytes = excelExportUtil.buildSingleExcelBytes(item.getOrderList());
            downloadFileName = item.getFileName() + ".xlsx";
            contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        } else {
            downloadBytes = excelExportUtil.buildZipBytes(itemList);
            downloadFileName = month.toString() + ".zip";
            contentType = "application/zip";
        }

        // 3.设置response下载头
        String encodeName = URLEncoder.encode(downloadFileName, StandardCharsets.UTF_8);
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + encodeName);

        // 4.输出二进制流给浏览器下载
        try (OutputStream os = response.getOutputStream()) {
            os.write(downloadBytes);
            os.flush();
        }
    }

    /*
    * 产品名称联想品名、规格1、规格2，前提是有customerId，processId与productName关键词
    * */
    @PostMapping("/suggest")
    public Result suggest(@RequestBody OrderListParam param) {
        if (param.getCustomerId() == null || param.getProcessId() == null || param.getProductName() == null || param.getProductName().isEmpty()) return Result.error("关键词联想参数错误");
        log.info("产品名称联想品名、规格1、规格2：{}", param);
        return Result.success(bizOrderService.keywordSuggest(param));
    }


    /*
    * AB面模式添加订单
    * */
    @PostMapping("/ABMode-add")
    @OperationLog("AB面模式添加订单")
    public Result ABModeAdd(@RequestBody OrderABModeParam orderABMode) {
        log.info("AB面模式添加订单: {}", orderABMode);
        bizOrderService.ABModeAdd(orderABMode);
        return Result.success();
    }

    /*
    *  AB面提交并打印
    * */
    @PostMapping("/ABMode-submit-and-print")
    @OperationLog("AB面提交并打印")
    public Result ABModeSubmitAndPrint(@RequestBody OrderABModeParam orderABMode) throws Exception {
        log.info("AB面提交并打印: {}", orderABMode);
        bizOrderService.ABModeSubmitAndPrint(orderABMode);
        return Result.success();
    }

    /*
    * 普通提交并打印
    * */
    @PostMapping("/submit-and-print")
    @OperationLog("普通提交并打印")
    public Result submitAndPrint(@RequestBody BizOrder order) throws Exception {
        log.info("普通提交并打印: {}", order);
        bizOrderService.submitAndPrint(order);
        return Result.success();
    }

    /*
    * 获取已删除订单的订单列表
    * 只能查询最近三十天的已删除订单
    * */
    @GetMapping("/list-deleted-orders")
    @RequireRole({2})
    public Result listDeletedOrders() {
        log.info("获取已删除订单的订单列表");
        return Result.success(bizOrderService.listDeletedOrders());
    }

    /*
    * 根据ids恢复订单
    * */
    @GetMapping("/restore-by-ids")
    @RequireRole({2})
    @OperationLog("根据ids恢复订单")
    public Result restoreByIds(@RequestParam List<Integer> ids) {
        log.info("根据ids恢复订单: {}", ids);
        bizOrderService.restoreByIds(ids);
        return Result.success();
    }
}
