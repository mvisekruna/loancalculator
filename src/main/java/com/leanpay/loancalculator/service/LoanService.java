package com.leanpay.loancalculator.service;

import com.leanpay.loancalculator.mapper.LoanMapper;
import com.leanpay.loancalculator.model.persistance.LoanEntity;
import com.leanpay.loancalculator.model.rest.LoanRequest;
import com.leanpay.loancalculator.model.rest.LoanResponse;
import com.leanpay.loancalculator.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class LoanService {

    private static final int MONTHS_IN_YEAR = 12;
    private static final int PERCENT_DIVISOR = 100;

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;

    public LoanResponse calculateLoan(LoanRequest loanRequest) {
        BigDecimal amount = loanRequest.getAmount();
        BigDecimal annualInterestPercent = loanRequest.getAnnualInterestPercent();
        int numberOfMonths = loanRequest.getNumberOfMonths();

        BigDecimal monthlyRate = annualInterestPercent.divide(BigDecimal.valueOf(PERCENT_DIVISOR), 10, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(MONTHS_IN_YEAR), 10, RoundingMode.HALF_UP);

        BigDecimal monthlyPayment = calculateMonthlyPayment(annualInterestPercent, amount, numberOfMonths, monthlyRate);
        BigDecimal totalPayment = monthlyPayment.multiply(BigDecimal.valueOf(numberOfMonths)).setScale(2, RoundingMode.HALF_UP);

        LoanEntity loanEntity = loanMapper.toEntity(amount, annualInterestPercent, numberOfMonths, monthlyPayment, totalPayment);
        loanRepository.save(loanEntity);

        return loanMapper.toResponse(monthlyPayment, totalPayment);
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
