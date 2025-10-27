package com.leanpay.loancalculator.model.rest;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class LoanResponse {

    private BigDecimal monthlyPayment;

    private BigDecimal totalPayment;

    private List<InstallmentDTO> installments;


}
