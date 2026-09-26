package com.songfu.controller;

import com.songfu.annotation.OperationLog;
import com.songfu.vo.Result;
import com.songfu.service.BizPrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/prints")
public class BizPrintController {

    @Autowired
    private BizPrintService bizPrintService;

    /*
    * 根据ids打印订单
    * */
    @GetMapping
    @OperationLog("根据ids打印订单")
    public Result printOrder(@RequestParam List<Integer> ids, @RequestParam Integer mode) throws Exception {
        bizPrintService.printOrder(ids, mode);
        return Result.success();
    }
}
