package com.songfu.controller;

import com.songfu.annotation.RequireRole;
import com.songfu.model.ExcelExportItem;
import com.songfu.pojo.SysOperationLog;
import com.songfu.utils.OperationExcelOperatorUtil;
import com.songfu.vo.Result;
import com.songfu.dto.LogParam;
import com.songfu.service.SysOperationLogService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/logs")
public class SysOperationLogController {

    @Autowired
    private SysOperationLogService sysOperationLogService;

    @Autowired
    private OperationExcelOperatorUtil operationExcelOperatorUtil;

    @PostMapping("/list")
    @RequireRole({2})
    public Result list(@RequestBody LogParam param) {
        log.info("查询操作日志：{}", param);
        return Result.success(sysOperationLogService.list(param));
    }

    /*
    * Excel导出
    * */
    @GetMapping("/export")
    @RequireRole({2})
    public void excelExport(@RequestParam LocalDate startDate,
                            @RequestParam LocalDate endDate,
                            HttpServletResponse response) throws IOException {
        // 1.调用service拿到Excel元数据
        ExcelExportItem<SysOperationLog> item = sysOperationLogService.excelExport(startDate, endDate);

        // 2.设置下载内容
        byte[] downloadBytes = operationExcelOperatorUtil.buildSingleExcelBytes(item.getList());
        String downloadFileName = item.getFileName() + ".xlsx";
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

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
}
