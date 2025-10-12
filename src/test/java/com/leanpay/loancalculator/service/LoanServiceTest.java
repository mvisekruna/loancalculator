package com.leanpay.loancalculator.service;

import com.leanpay.loancalculator.mapper.LoanMapper;
import com.leanpay.loancalculator.model.persistance.LoanEntity;
import com.leanpay.loancalculator.model.rest.LoanRequest;
import com.leanpay.loancalculator.model.rest.LoanResponse;
import com.leanpay.loancalculator.repository.LoanRepository;
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
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanMapper loanMapper;

    @InjectMocks
    private LoanService loanService;

    @Test
    void calculateLoan_withInterest_shouldReturnCorrectValues() {
        LoanRequest request = new LoanRequest();
        request.setAmount(BigDecimal.valueOf(1000));
        request.setAnnualInterestPercent(BigDecimal.valueOf(5));
        request.setNumberOfMonths(10);

        when(loanMapper.toEntity(any(), any(), anyInt(), any(), any())).thenReturn(new LoanEntity());
        when(loanMapper.toResponse(any(), any())).thenAnswer(invocation -> {
            LoanResponse response = new LoanResponse();
            response.setMonthlyPayment(invocation.getArgument(0));
            response.setTotalPayment(invocation.getArgument(1));
            return response;
        });

        LoanResponse response = loanService.calculateLoan(request);

        assertThat(response.getMonthlyPayment()).isEqualByComparingTo("102.31");
        assertThat(response.getTotalPayment()).isEqualByComparingTo("1023.10");

        verify(loanRepository).save(any(LoanEntity.class));
        verify(loanMapper).toEntity(any(), any(), anyInt(), any(), any());
        verify(loanMapper).toResponse(any(), any());
    }

    @Test
    void calculateLoan_zeroInterest_shouldReturnSimpleDivision() {
        LoanRequest request = new LoanRequest();
        request.setAmount(BigDecimal.valueOf(1200));
        request.setAnnualInterestPercent(BigDecimal.ZERO);
        request.setNumberOfMonths(6);

        when(loanMapper.toEntity(any(), any(), anyInt(), any(), any())).thenReturn(new LoanEntity());
        when(loanMapper.toResponse(any(), any())).thenAnswer(invocation -> {
            LoanResponse response = new LoanResponse();
            response.setMonthlyPayment(invocation.getArgument(0));
            response.setTotalPayment(invocation.getArgument(1));
            return response;
        });

        LoanResponse response = loanService.calculateLoan(request);

        assertThat(response.getMonthlyPayment()).isEqualByComparingTo("200.00");
        assertThat(response.getTotalPayment()).isEqualByComparingTo("1200.00");

        verify(loanRepository).save(any(LoanEntity.class));
        verify(loanMapper).toEntity(any(), any(), anyInt(), any(), any());
        verify(loanMapper).toResponse(any(), any());
    }
}