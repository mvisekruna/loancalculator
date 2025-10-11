package com.leanpay.loancalculator.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanRequest {

    private BigDecimal amount;

    private BigDecimal annualInterestPercent;

    private int numberOfMonths;

}
