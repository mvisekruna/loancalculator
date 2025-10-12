package com.leanpay.loancalculator.mapper;

import com.leanpay.loancalculator.model.persistance.LoanEntity;
import com.leanpay.loancalculator.model.rest.LoanResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class LoanMapper {

    public LoanResponse toResponse(BigDecimal monthlyPayment, BigDecimal totalPayment) {
        LoanResponse response = new LoanResponse();
        response.setMonthlyPayment(monthlyPayment);
        response.setTotalPayment(totalPayment);
        return response;
    }

    public LoanEntity toEntity(BigDecimal amount, BigDecimal annualInterestPercent, int numberOfMonths, BigDecimal monthlyPayment, BigDecimal totalPayment) {
        LoanEntity entity = new LoanEntity();
        entity.setAmount(amount);
        entity.setAnnualInterestPercent(annualInterestPercent);
        entity.setNumberOfMonths(numberOfMonths);
        entity.setMonthlyPayment(monthlyPayment);
        entity.setTotalPayment(totalPayment);
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

}
