package com.songfu.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
/*
* 订单列表条件分页查询参数
* */
@Data
public class OrderListParam {
    private Integer id;  // 订单id
    private String productName;  // 产品名称
    private Integer customerId;  // 客户id
    private Integer processId;  // 工艺id
    private LocalDate beginDate;  // 开始日期
    private LocalDate endDate;  // 结束日期
    private Boolean isSpecialUnitPrice;  // 是否特殊单价
    private BigDecimal beginAmount;  // 开始金额
    private BigDecimal endAmount;  // 结束金额
    private Boolean isPrinted;  // 是否打印
    private Integer pageNum = 1;  // 当前页码
    private Integer pageSize = 10;  // 每页大小
}
