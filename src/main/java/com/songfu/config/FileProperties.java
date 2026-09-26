package com.songfu.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "file")

/*
* 文件存储路径配置类
* */
public class FileProperties {
    /*
    * Excel打印文件保存路径
    * */
    private String excelSavePath;

    /*
    * PDF文件保存路径
    * */
    private String pdfSavePath;

    /*
    * 三联表单模板
    * */
    private String triplicateFormTemplate;

    /*
    * 四联表单模板
    * */
    private String quadruplicateFormTemplate;
}
