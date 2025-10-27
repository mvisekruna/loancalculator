package com.leanpay.loancalculator.service.implementation;


import com.leanpay.loancalculator.model.rest.InstallmentDTO;
import com.leanpay.loancalculator.service.LoanCalculationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanCalculationServiceImplementation implements LoanCalculationService {

    private static final int MONTHS_IN_YEAR = 12;
    private static final int PERCENT_DIVISOR = 100;

    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal annualInterestPercent, int numberOfMonths) {
        BigDecimal monthlyRate = annualInterestPercent
                .divide(BigDecimal.valueOf(PERCENT_DIVISOR), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(MONTHS_IN_YEAR), 10, RoundingMode.HALF_UP);

        if (annualInterestPercent.signum() == 0) {
            return amount.divide(BigDecimal.valueOf(numberOfMonths), 2, RoundingMode.HALF_UP);
        }

        BigDecimal rateFactor = (monthlyRate.add(BigDecimal.ONE)).pow(numberOfMonths);
        BigDecimal numerator = amount.multiply(monthlyRate).multiply(rateFactor);
        BigDecimal denominator = rateFactor.subtract(BigDecimal.ONE);

        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

    @Override
    public List<InstallmentDTO> generateInstallments(BigDecimal amount, BigDecimal monthlyRate, BigDecimal monthlyPayment, int numberOfMonths) {
        BigDecimal balance = amount;
        List<InstallmentDTO> installments = new ArrayList<>();

        for (int i = 1; i <= numberOfMonths; i++) {
            BigDecimal interest = balance.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principal = monthlyPayment.subtract(interest).setScale(2, RoundingMode.HALF_UP);
            balance = balance.subtract(principal).setScale(2, RoundingMode.HALF_UP);

            InstallmentDTO dto = new InstallmentDTO();
            dto.setPeriodNumber(i);
            dto.setPaymentAmount(monthlyPayment);
            dto.setPrincipalAmount(principal);
            dto.setInterestAmount(interest);
            dto.setBalanceOwed(balance);
            installments.add(dto);
        }

        if (!installments.isEmpty()) {
            InstallmentDTO last = installments.get(installments.size() - 1);
            if (last.getBalanceOwed().abs().compareTo(BigDecimal.valueOf(0.05)) < 0) {
                last.setBalanceOwed(BigDecimal.ZERO);
            }
        }

        return installments;
    }
}
