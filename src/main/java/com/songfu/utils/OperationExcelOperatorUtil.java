package com.songfu.utils;

import com.songfu.pojo.SysOperationLog;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class OperationExcelOperatorUtil {

    public XSSFWorkbook buildSingleWorkbook(List<SysOperationLog> operationLogList){
        // 1.创建工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 2.创建工作表
        XSSFSheet sheet = workbook.createSheet("操作日志");

        // 3.构建表头
        XSSFRow headerRow = sheet.createRow(0);
        String[] headers = {"ID", "用户ID", "用户名", "操作描述", "方法定位", "参数", "IP地址", "是否成功", "错误信息", "耗时", "操作时间"};
        for (int colIndex = 0; colIndex < headers.length; colIndex++){
            headerRow.createCell(colIndex).setCellValue(headers[colIndex]);
        }

        // 5.循环写入业务数据
        int poiRowIndex = 1; // POI索引，从0开始
        for (SysOperationLog log : operationLogList) {
            XSSFRow row = sheet.createRow(poiRowIndex);

            // 写入数据
            row.createCell(0).setCellValue(log.getId());
            row.createCell(1).setCellValue(log.getUserId() == null ? "" : log.getUserId().toString());
            row.createCell(2).setCellValue(log.getUsername());
            row.createCell(3).setCellValue(log.getOperation());
            row.createCell(4).setCellValue(log.getMethod());
            row.createCell(5).setCellValue(log.getParams());
            row.createCell(6).setCellValue(log.getIp());
            row.createCell(7).setCellValue(log.getStatus() == 1 ? "成功" : "失败");
            row.createCell(8).setCellValue(log.getErrorMsg());
            row.createCell(9).setCellValue(log.getCostTime());
            row.createCell(10).setCellValue(log.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            poiRowIndex++;
        }

        // 7.设置列宽度
        sheet.setColumnWidth(0, 6 * 256);
        sheet.setColumnWidth(1, 7 * 256);
        sheet.setColumnWidth(2, 8 * 256);
        sheet.setColumnWidth(3, 30 * 256);
        sheet.setColumnWidth(4, 60 * 256);
        sheet.setColumnWidth(5, 60 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 9 * 256);
        sheet.setColumnWidth(8, 30 * 256);
        sheet.setColumnWidth(9, 8 * 256);
        sheet.setColumnWidth(10, 20 * 256);

        return workbook;
    }

    public byte[] buildSingleExcelBytes(List<SysOperationLog> operationLogList) throws IOException {
        try (XSSFWorkbook workbook = buildSingleWorkbook(operationLogList);
                ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            workbook.write(bos);
            return bos.toByteArray();
        }
    }
}
