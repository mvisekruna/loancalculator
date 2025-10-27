package com.leanpay.loancalculator.mapper;

import com.leanpay.loancalculator.model.persistance.InstallmentEntity;
import com.leanpay.loancalculator.model.persistance.LoanEntity;
import com.leanpay.loancalculator.model.rest.InstallmentDTO;
import com.leanpay.loancalculator.model.rest.LoanResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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

    public LoanResponse toResponse(LoanEntity entity) {
        LoanResponse response = new LoanResponse();
        response.setMonthlyPayment(entity.getMonthlyPayment());
        response.setTotalPayment(entity.getTotalPayment());
        response.setInstallments(Collections.emptyList());
        return response;
    }

    public LoanEntity toEntity(BigDecimal amount, BigDecimal annualInterestPercent,
                               int numberOfMonths, BigDecimal monthlyPayment,
                               BigDecimal totalPayment, List<InstallmentDTO> installments) {

        LoanEntity entity = new LoanEntity();
        entity.setAmount(amount);
        entity.setAnnualInterestPercent(annualInterestPercent);
        entity.setNumberOfMonths(numberOfMonths);
        entity.setMonthlyPayment(monthlyPayment);
        entity.setTotalPayment(totalPayment);
        entity.setCreatedAt(LocalDateTime.now());

        List<InstallmentEntity> installmentEntities = installments.stream()
                .map(dto -> {
                    InstallmentEntity inst = new InstallmentEntity();
                    inst.setPeriodNumber(dto.getPeriodNumber());
                    inst.setPaymentAmount(dto.getPaymentAmount());
                    inst.setPrincipalAmount(dto.getPrincipalAmount());
                    inst.setInterestAmount(dto.getInterestAmount());
                    inst.setBalanceOwed(dto.getBalanceOwed());
                    inst.setLoan(entity);
                    return inst;
                })
                .toList();

        entity.setInstallments(installmentEntities);

        return entity;
    }


}
