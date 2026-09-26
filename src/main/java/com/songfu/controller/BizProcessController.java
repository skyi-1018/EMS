package com.songfu.controller;

import com.songfu.annotation.OperationLog;
import com.songfu.annotation.RequireRole;
import com.songfu.pojo.BizProcess;
import com.songfu.vo.Result;
import com.songfu.service.BizProcessService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/processes")
@RestController
public class BizProcessController {

    @Autowired
    private BizProcessService bizProcessService;

    /*
    * 工艺信息列表查询
    * */
    @GetMapping
    public Result list(){
        log.info("工艺信息列表查询");
        return Result.success(bizProcessService.list());
    }

    /*
    * 修改工艺
    * */
    @PutMapping
    @OperationLog("修改工艺")
    @RequireRole({2})
    public Result update(@Valid @RequestBody BizProcess process){
        log.info("修改工艺: {}", process);
        bizProcessService.update(process);
        return Result.success();
    }

    /*
    * 根据id删除工艺*
    * */
    @DeleteMapping
    @OperationLog("根据id删除工艺")
    @RequireRole({2})
    public Result deleteById(Integer id){
        log.info("根据id删除工艺: {}", id);
        bizProcessService.deleteById(id);
        return Result.success();
    }

    /*
    * 添加工艺
    * */
    @PostMapping
    @OperationLog("添加工艺")
    @RequireRole({2})
    public Result add(@Valid @RequestBody BizProcess process){
        log.info("添加工艺: {}", process);
        bizProcessService.add(process);
        return Result.success();
    }
}
