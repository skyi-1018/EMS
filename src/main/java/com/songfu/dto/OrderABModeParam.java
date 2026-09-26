package com.songfu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OrderABModeParam {
    @NotNull(message = "客户ID不能为空")
    private Integer customerId;
    @NotNull(message = "工艺ID不能为空")
    private Integer processId;
    @NotNull(message = "订单日期不能为空")
    private LocalDate orderDate;
    @NotBlank(message = "产品名称不能为空")
    private String productName;
    private BigDecimal specialUnitPrice;
    private String remark;

    // ABMode
    @NotNull(message = "A面规格1不能为空")
    private BigDecimal spec1A;
    @NotNull(message = "A面规格2不能为空")
    private BigDecimal spec2A;
    @NotNull(message = "A面数量不能为空")
    private Integer quantityA;
    @NotNull(message = "A面金额不能为空")
    private BigDecimal amountA;

    @NotNull(message = "B面规格1不能为空")
    private BigDecimal spec1B;
    @NotNull(message = "B面规格2不能为空")
    private BigDecimal spec2B;
    @NotNull(message = "B面数量不能为空")
    private Integer quantityB;
    @NotNull(message = "B面金额不能为空")
    private BigDecimal amountB;

    // 封装打印参数
    private Integer mode;
}
