package com.songfu.utils;

import com.songfu.common.UserContext;
import com.songfu.config.FileProperties;
import com.songfu.pojo.BizOrder;
import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class PrintUtil {

    @Autowired
    private FileProperties fileProperties;

    /**
     * 打印订单，传入订单信息与打印人
     * @param orderList 同一客户订单列表
     * @param mode 1三联单 2四联单
     * @throws Exception 异常
     */
    public void printOrder(List<BizOrder> orderList, Integer mode) throws Exception {
        // 判空，防止get(0)空指针
        if (orderList == null || orderList.isEmpty()) {
            throw new RuntimeException("请选择至少一条订单");
        }

        String preparer = UserContext.getUsername();
        String templatePath;

        Integer baseCustomerId = orderList.get(0).getCustomerId();
        if (baseCustomerId == null) {
            throw new RuntimeException("订单客户ID不能为空");
        }

        // 校验全部订单属于同一个客户
        for (BizOrder order : orderList) {
            if (!baseCustomerId.equals(order.getCustomerId())) {
                throw new RuntimeException("所选订单不属于同一个客户，不允许打印");
            }
        }

        // 处理特价逻辑
        String customerName = orderList.get(0).getCustomerName();
        orderList.forEach(order -> {
            if (order.getSpecialUnitPrice() != null && order.getSpecialUnitPrice().compareTo(java.math.BigDecimal.ZERO) > 0) {
                order.setUnitPrice(order.getSpecialUnitPrice());
                String remark = order.getRemark() == null ? "" : order.getRemark();
                order.setRemark(remark + "特价单");
            }
        });

        // 选择模板文件
        if (mode == 1) {
            templatePath = fileProperties.getTriplicateFormTemplate();
        } else if (mode == 2) {
            templatePath = fileProperties.getQuadruplicateFormTemplate();
        } else {
            throw new RuntimeException("打印模式错误，仅支持1(三联)、2(四联)");
        }

        OdfSpreadsheetDocument odf = OdsTemplateUtil.fillTemplate(templatePath, orderList, customerName, preparer);

        // 生成临时ods文件
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = customerName + "_" + sdf.format(new Date());

        File saveDir = new File(fileProperties.getExcelSavePath());
        if (!saveDir.exists()) {
            boolean mkdirOk = saveDir.mkdirs();
        }
        File outFile = new File(saveDir, fileName + ".ods");

        // odfdom标准保存，不需要FileOutputStream
        odf.save(outFile);
        odf.close();

        String savePath = outFile.getAbsolutePath();

        // LibreOffice headless 转换PDF
        List<String> loCmd = List.of(
                "libreoffice",
                "--headless",
                "--convert-to", "pdf",
                savePath,
                "--outdir", fileProperties.getPdfSavePath()
        );
        CmdUtil.runCommand(loCmd);

        File pdfDir = new File(fileProperties.getPdfSavePath());
        File pdfFile = new File(pdfDir, fileName + ".pdf");
        String pdfTmp = pdfFile.getAbsolutePath();

        // 8.lp CUPS打印，纸张221x93mm
        List<String> lpCmd = List.of(
                "lp",
                "-o", "media=Custom.221x93mm",
                "-o", "scaling=100",
                "-o", "fit-to-page=false",
                pdfTmp
        );
        CmdUtil.runCommand(lpCmd);
    }
}
