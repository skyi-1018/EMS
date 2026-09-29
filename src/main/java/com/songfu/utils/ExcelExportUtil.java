package com.songfu.utils;

import com.songfu.pojo.BizOrder;
import com.songfu.model.ExcelExportItem;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class ExcelExportUtil {

    @Autowired
    private ExcelOperatorUtil excelOperatorUtil;


    /*
    * 生成单个xlsx文件字节数组
    * try-with-resources自动关闭workbook，释放POI资源
    * */
    public byte[] buildSingleExcelBytes(List<BizOrder> orderList) throws IOException {
        try (XSSFWorkbook workbook = excelOperatorUtil.buildSingleWorkbook(orderList);
                ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            workbook.write(bos);
            return bos.toByteArray();
        }
    }


    /*
    * 多个Excel打包为zip压缩包字节数组
    * */
    public byte[] buildZipBytes(List<ExcelExportItem> itemList) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream((bos))) {
            for (ExcelExportItem item : itemList) {
                XSSFWorkbook workbook = excelOperatorUtil.buildSingleWorkbook(item.getList());
                String safeFileName = item.getFileName().replaceAll("[\\\\/:*?\"<>|]", "_");
                zos.putNextEntry(new ZipEntry(safeFileName + ".xlsx"));
                workbook.write(zos);
                workbook.close();
                zos.closeEntry();
            }
        }
        return bos.toByteArray();
    }
}
