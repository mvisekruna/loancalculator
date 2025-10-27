package com.leanpay.loancalculator.service.implementation;

import com.leanpay.loancalculator.mapper.LoanMapper;
import com.leanpay.loancalculator.model.persistance.LoanEntity;
import com.leanpay.loancalculator.model.rest.InstallmentDTO;
import com.leanpay.loancalculator.model.rest.LoanRequest;
import com.leanpay.loancalculator.model.rest.LoanResponse;
import com.leanpay.loancalculator.repository.LoanRepository;
import com.leanpay.loancalculator.service.LoanCalculationService;
import com.leanpay.loancalculator.service.LoanService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanServiceImplementation implements LoanService {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final LoanCalculationService loanCalculationService;

    @Transactional
    @Override
    public LoanResponse calculateLoan(LoanRequest loanRequest) {
        String requestHash = generateRequestHash(loanRequest);

        Optional<LoanEntity> existing = loanRepository.findByRequestHash(requestHash);
        if (existing.isPresent()) {
            return loanMapper.toResponse(existing.get());
        }

        BigDecimal amount = loanRequest.getAmount();
        BigDecimal annualInterestPercent = loanRequest.getAnnualInterestPercent();
        int numberOfMonths = loanRequest.getNumberOfMonths();

        BigDecimal monthlyPayment = loanCalculationService.calculateMonthlyPayment(amount, annualInterestPercent, numberOfMonths);

        BigDecimal monthlyRate = annualInterestPercent
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        List<InstallmentDTO> installments =
                loanCalculationService.generateInstallments(amount, monthlyRate, monthlyPayment, numberOfMonths);

        BigDecimal totalPayment = monthlyPayment.multiply(BigDecimal.valueOf(numberOfMonths)).setScale(2, RoundingMode.HALF_UP);

        LoanEntity loanEntity = loanMapper.toEntity(amount, annualInterestPercent, numberOfMonths, monthlyPayment, totalPayment, installments);
        loanEntity.setRequestHash(requestHash);
        loanRepository.save(loanEntity);

        LoanResponse response = loanMapper.toResponse(monthlyPayment, totalPayment);
        response.setInstallments(installments);
        return response;
    }

    private String generateRequestHash(LoanRequest loanRequest) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = loanRequest.getAmount() + ":" +
                    loanRequest.getAnnualInterestPercent() + ":" +
                    loanRequest.getNumberOfMonths();
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Failed to generate request hash", e);
        }
    }

}
