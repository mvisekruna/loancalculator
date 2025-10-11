package com.leanpay.loancalculator.service;

import com.leanpay.loancalculator.model.LoanEntity;
import com.leanpay.loancalculator.model.LoanRequest;
import com.leanpay.loancalculator.model.LoanResponse;
import com.leanpay.loancalculator.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;

    public LoanResponse calculateLoan(LoanRequest loanRequest) {
        BigDecimal amount = loanRequest.getAmount();
        BigDecimal annualInterestPercent = loanRequest.getAnnualInterestPercent();
        int numberOfMonths = loanRequest.getNumberOfMonths();

        if (amount.compareTo(BigDecimal.ZERO) <= 0 || numberOfMonths <= 0) {
            throw new IllegalArgumentException("Amount and number of months must be positive.");
        }

        BigDecimal monthlyRate = annualInterestPercent.divide(BigDecimal.valueOf(12L * 100), 10, RoundingMode.HALF_UP);

        BigDecimal monthlyPayment;
        BigDecimal totalPayment;

        monthlyPayment = calculateMonthlyPayment(annualInterestPercent, amount, numberOfMonths, monthlyRate);
        totalPayment = monthlyPayment.multiply(BigDecimal.valueOf(numberOfMonths)).setScale(2, RoundingMode.HALF_UP);

        LoanEntity loanEntity = buildLoanEntity(amount, annualInterestPercent, numberOfMonths, monthlyPayment, totalPayment);
        loanRepository.save(loanEntity);

        return buildLoanResponse(monthlyPayment, totalPayment);
    }

    private static LoanResponse buildLoanResponse(BigDecimal monthlyPayment, BigDecimal totalPayment) {
        LoanResponse loanResponse = new LoanResponse();
        loanResponse.setMonthlyPayment(monthlyPayment);
        loanResponse.setTotalPayment(totalPayment);
        return loanResponse;
    }

    private LoanEntity buildLoanEntity(BigDecimal amount, BigDecimal annualInterestPercent, int numberOfMonths, BigDecimal monthlyPayment, BigDecimal totalPayment) {
        LoanEntity loanEntity = new LoanEntity();

        loanEntity.setAmount(amount);
        loanEntity.setAnnualInterestPercent(annualInterestPercent);
        loanEntity.setNumberOfMonths(numberOfMonths);
        loanEntity.setMonthlyPayment(monthlyPayment);
        loanEntity.setTotalPayment(totalPayment);
        loanEntity.setCreatedAt(LocalDateTime.now());

        return loanEntity;
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal annualInterestPercent, BigDecimal amount, int numberOfMonths, BigDecimal monthlyRate) {
        if (annualInterestPercent.signum() == 0) {
            return amount.divide(BigDecimal.valueOf(numberOfMonths), 2, RoundingMode.HALF_UP);
        }

        BigDecimal rateFactor = (monthlyRate.add(BigDecimal.valueOf(1))).pow(numberOfMonths);
        BigDecimal numerator = amount.multiply(monthlyRate).multiply(rateFactor);
        BigDecimal denominator = rateFactor.subtract(BigDecimal.valueOf(1));

        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

}
