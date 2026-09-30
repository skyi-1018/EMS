package com.songfu.utils;

import com.songfu.pojo.BizOrder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ExcelOperatorUtil {

    public XSSFWorkbook buildSingleWorkbook(List<BizOrder> orderList){
        // 1.创建工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 2.创建工作表
        XSSFSheet sheet = workbook.createSheet("订单明细");

        // 3.样式设置
        XSSFCellStyle numberCellStyle_0 = workbook.createCellStyle();
        DataFormat dataFormat = workbook.createDataFormat();
        numberCellStyle_0.setDataFormat(dataFormat.getFormat("0_);-0"));

        XSSFCellStyle numberCellStyle_1 = workbook.createCellStyle();
        numberCellStyle_1.setDataFormat(dataFormat.getFormat("0.0_);-0.0"));

        XSSFCellStyle numberCellStyle_2 = workbook.createCellStyle();
        numberCellStyle_2.setDataFormat(dataFormat.getFormat("0.00_);-0.00"));

        XSSFCellStyle numberCellStyle_3 = workbook.createCellStyle();
        numberCellStyle_3.setDataFormat(dataFormat.getFormat("0.000_);-0.000"));

        XSSFCellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(dataFormat.getFormat("yyyy年MM月dd日"));

        // 4.构建表头
        XSSFRow headerRow = sheet.createRow(0);
        String[] headers = {"客户", "工艺", "产品名称", "日期", "单号", "规格一", "规格二", "数量", "单价", "金额", "备注"};
        for (int colIndex = 0; colIndex < headers.length; colIndex++){
            headerRow.createCell(colIndex).setCellValue(headers[colIndex]);
        }

        // 5.循环写入业务数据
        int poiRowIndex = 1; // POI索引，从0开始
        for (BizOrder order : orderList) {
            XSSFRow row = sheet.createRow(poiRowIndex);
            int excelShowRow = poiRowIndex + 1; //实际行号

            // 写入数据
            row.createCell(0).setCellValue(order.getCustomerName());

            row.createCell(1).setCellValue(order.getProcessName());

            row.createCell(2).setCellValue(order.getProductName());

            Cell orderDate = row.createCell(3);
            orderDate.setCellValue(order.getOrderDate());
            orderDate.setCellStyle(dateStyle);

            row.createCell(4).setCellValue(order.getId());

            Cell spec1 = row.createCell(5);  // F
            spec1.setCellValue(order.getSpec1().doubleValue());
            spec1.setCellStyle(numberCellStyle_1);

            Cell spec2 = row.createCell(6);  // G
            spec2.setCellValue(order.getSpec2().doubleValue());
            spec2.setCellStyle(numberCellStyle_1);

            Cell quantity = row.createCell(7);  // H
            quantity.setCellValue(order.getQuantity());
            quantity.setCellStyle(numberCellStyle_0);

            Cell unitPrice = row.createCell(8);  // I
            String remark = order.getRemark();
            if (order.getSpecialUnitPrice() != null && order.getSpecialUnitPrice().compareTo(BigDecimal.ZERO) != 0) {
                order.setUnitPrice(order.getSpecialUnitPrice());
                remark += "(特价)";
            }
            unitPrice.setCellValue(order.getUnitPrice().doubleValue());
            unitPrice.setCellStyle(numberCellStyle_3);

            Cell totalCell = row.createCell(9);
            totalCell.setCellFormula("ROUND(F" + excelShowRow + "*G" + excelShowRow + "*H" + excelShowRow + "*I" + excelShowRow + "*0.0001, 2)");  // 修复精度问题
            totalCell.setCellStyle(numberCellStyle_2);
            row.createCell(10).setCellValue(remark);

            poiRowIndex++;
        }

        // 6.末尾行合计
        if (!orderList.isEmpty()){
            XSSFRow sumRow = sheet.createRow(poiRowIndex);
            sumRow.createCell(8).setCellValue("合计");

            Cell sumCell = sumRow.createCell(9);
            sumCell.setCellFormula("SUM(J2:J" + poiRowIndex + ")");
            sumCell.setCellStyle(numberCellStyle_2);
        }

        // 7.设置列宽度
        sheet.setColumnWidth(0, 12 * 256);
        sheet.setColumnWidth(1, 12 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        sheet.setColumnWidth(3, 17 * 256);
        sheet.setColumnWidth(4, 8 * 256);
        sheet.setColumnWidth(5, 8 * 256);
        sheet.setColumnWidth(6, 8 * 256);
        sheet.setColumnWidth(7, 9 * 256);
        sheet.setColumnWidth(8, 7 * 256);
        sheet.setColumnWidth(9, 11 * 256);
        sheet.setColumnWidth(10, 40 * 256);

        return workbook;
    }
}
