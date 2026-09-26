package com.songfu.model;

import com.songfu.pojo.BizOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelExportItem {

    private String fileName;
    private List<BizOrder> orderList;
}
