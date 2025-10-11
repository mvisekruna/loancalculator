package com.leanpay.loancalculator.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanResponse {

    private BigDecimal monthlyPayment;

    private BigDecimal totalPayment;

}
