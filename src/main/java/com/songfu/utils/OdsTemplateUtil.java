package com.songfu.utils;

import com.songfu.pojo.BizOrder;
import lombok.extern.slf4j.Slf4j;
import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OdsTemplateUtil {

    /**
     * 填充ODS模板
     * @param templatePath 模板磁盘路径；classpath资源请传入InputStream重载方法
     * @param orderList 订单数据
     * @param customerName 客户名称
     * @param preparer 制单人
     * @return OdfSpreadsheetDocument 填充完成的文档，调用方负责close()
     * @throws IOException 文件异常
     */
    public static OdfSpreadsheetDocument fillTemplate(String templatePath,
                                                      List<BizOrder> orderList,
                                                      String customerName,
                                                      String preparer) throws Exception {
        File templateFile = new File(templatePath);
        if (!templateFile.exists()) {
            throw new RuntimeException("模板文件不存在：" + templatePath);
        }
        try (InputStream is = new FileInputStream(templateFile)) {
            return fillTemplate(is, orderList, customerName, preparer);
        }
    }

    /**
     * 重载：InputStream版本，支持classpath资源读取（jar包内模板）
     */
    public static OdfSpreadsheetDocument fillTemplate(InputStream templateIs,
                                                      List<BizOrder> orderList,
                                                      String customerName,
                                                      String preparer) throws Exception {

        long startTotal = System.currentTimeMillis();

        // 1. 加载文档
        OdfSpreadsheetDocument spreadsheetDoc = OdfSpreadsheetDocument.loadDocument(templateIs);

        // 2. 获取表格
        OdfTable table = spreadsheetDoc.getSpreadsheetTables().get(0);

        // 3. 循环写入订单数据
        int startRowIndex = 3;
        double totalAmount = 0.0D;

        StringBuilder totalRemark = new StringBuilder();
        List<String> remarkList = new ArrayList<>();

        // 循环写入订单
        for (int i = 0; i < orderList.size(); i++) {
            BizOrder order = orderList.get(i);
            int currentRow = startRowIndex + i;

            setCellValue(table, currentRow, 0, order.getId());
            setCellValue(table, currentRow, 1, order.getOrderDate());
            setCellValue(table, currentRow, 2, order.getProductName());
            setCellValue(table, currentRow, 3, order.getProcessName());
            setCellValue(table, currentRow, 4, order.getSpec1().doubleValue());
            setCellValue(table, currentRow, 5, order.getSpec2().doubleValue());
            setCellValue(table, currentRow, 6, order.getUnitPrice().doubleValue());
            setCellValue(table, currentRow, 7, order.getQuantity());
            setCellValue(table, currentRow, 8, order.getAmount().doubleValue());

            totalAmount += order.getAmount().doubleValue();

            String remark = order.getRemark();
            if (remark != null && !remark.isBlank()) {
                remarkList.add(order.getId().toString() + ": " + remark);
            }
        }
        totalRemark.append(String.join("\n", remarkList));

        setCellValue(table, 1, 1, customerName);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateStr = now.format(formatter);
        setCellValue(table, 1, 7, dateStr);

        setCellValue(table, 5, 8, totalAmount);
        setCellValue(table, 6, 1, totalRemark.toString());
        setCellValue(table, 7, 2, preparer);

        return spreadsheetDoc;
    }


    /**
     * ODS单元格写入工具，保留原有模板样式
     * @param table OdfTable
     * @param rowIndex  odf行下标【从0开始】
     * @param colIndex  odf列下标【从0开始】
     * @param value 值
     */
    public static void setCellValue(OdfTable table, int rowIndex, int colIndex, Object value) {
        OdfTableCell cell = table.getCellByPosition(colIndex, rowIndex);

        if (value == null) {
            cell.setStringValue("");
            return;
        }
        if (value instanceof String strVal) {
            cell.setStringValue(strVal);
        } else if (value instanceof Integer intVal) {
            cell.setDoubleValue(intVal.doubleValue());
        } else if (value instanceof BigDecimal bigDecimal) {
            cell.setDoubleValue(bigDecimal.doubleValue());
        } else if (value instanceof Double doubleVal) {
            cell.setDoubleValue(doubleVal);
        } else if (value instanceof LocalDateTime dateTime) {
            cell.setStringValue(dateTime.toString());
        } else {
            cell.setStringValue(String.valueOf(value));
        }
    }

}
