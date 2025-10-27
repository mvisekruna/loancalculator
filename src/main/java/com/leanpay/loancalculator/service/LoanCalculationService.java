package com.leanpay.loancalculator.service;


import com.leanpay.loancalculator.model.rest.InstallmentDTO;

import java.math.BigDecimal;
import java.util.List;

public interface LoanCalculationService {

    BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal annualInterestPercent, int numberOfMonths);

    List<InstallmentDTO> generateInstallments(BigDecimal amount, BigDecimal monthlyRate, BigDecimal monthlyPayment, int numberOfMonths);
}
