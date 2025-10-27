package com.leanpay.loancalculator.service;

import com.leanpay.loancalculator.mapper.LoanMapper;
import com.leanpay.loancalculator.model.persistance.LoanEntity;
import com.leanpay.loancalculator.model.rest.LoanRequest;
import com.leanpay.loancalculator.model.rest.LoanResponse;
import com.leanpay.loancalculator.repository.LoanRepository;
import com.leanpay.loancalculator.service.implementation.LoanServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplementationTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanMapper loanMapper;

    @InjectMocks
    private LoanServiceImplementation loanServiceImplementation;

    @BeforeEach
    void setUp() {
        when(loanMapper.toResponse(any(), any())).thenAnswer(invocation -> {
            LoanResponse response = new LoanResponse();
            response.setMonthlyPayment(invocation.getArgument(0));
            response.setTotalPayment(invocation.getArgument(1));
            return response;
        });
    }

    @Test
    void calculateLoan_withInterest_shouldReturnCorrectValues() {
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal interest = BigDecimal.valueOf(5);
        int months = 10;

        LoanRequest request = new LoanRequest();
        request.setAmount(amount);
        request.setAnnualInterestPercent(interest);
        request.setNumberOfMonths(months);

        when(loanMapper.toEntity(eq(amount), eq(interest), eq(months), any(), any()))
                .thenReturn(new LoanEntity());

        LoanResponse response = loanServiceImplementation.calculateLoan(request);

        assertThat(response.getMonthlyPayment()).isEqualByComparingTo("102.31");
        assertThat(response.getTotalPayment()).isEqualByComparingTo("1023.10");

        verify(loanRepository).save(any(LoanEntity.class));
        verify(loanMapper).toEntity(eq(amount), eq(interest), eq(months), any(), any());
        verify(loanMapper).toResponse(any(), any());
    }

    @Test
    void calculateLoan_zeroInterest_shouldReturnSimpleDivision() {
        BigDecimal amount = BigDecimal.valueOf(1200);
        BigDecimal interest = BigDecimal.ZERO;
        int months = 6;

        LoanRequest request = new LoanRequest();
        request.setAmount(amount);
        request.setAnnualInterestPercent(interest);
        request.setNumberOfMonths(months);

        when(loanMapper.toEntity(eq(amount), eq(interest), eq(months), any(), any()))
                .thenReturn(new LoanEntity());

        LoanResponse response = loanServiceImplementation.calculateLoan(request);

        assertThat(response.getMonthlyPayment()).isEqualByComparingTo("200.00");
        assertThat(response.getTotalPayment()).isEqualByComparingTo("1200.00");

        verify(loanRepository).save(any(LoanEntity.class));
        verify(loanMapper).toEntity(eq(amount), eq(interest), eq(months), any(), any());
        verify(loanMapper).toResponse(any(), any());
    }
}