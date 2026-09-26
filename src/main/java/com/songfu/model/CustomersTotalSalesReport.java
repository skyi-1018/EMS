package com.songfu.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomersTotalSalesReport {
    private Integer customerId;
    private String orderDate;
    private BigDecimal totalAmount;
}
