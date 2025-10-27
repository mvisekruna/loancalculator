package com.leanpay.loancalculator.model.rest;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InstallmentDTO {

    private Integer periodNumber;
    private BigDecimal paymentAmount;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal balanceOwed;

}
