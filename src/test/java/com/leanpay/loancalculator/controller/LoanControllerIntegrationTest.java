package com.leanpay.loancalculator.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LoanControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void calculateLoan_validRequest_shouldReturn200AndCorrectValues() throws Exception {
        mockMvc.perform(post("/api/v1/loan/calculate").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "amount": 1000,
                  "annualInterestPercent": 5,
                  "numberOfMonths": 10
                }
                """)).andExpect(status().isOk()).andExpect(jsonPath("$.monthlyPayment").value(102.31)).andExpect(jsonPath("$.totalPayment").value(1023.10));
    }

    @Test
    void calculateLoan_zeroInterest_shouldReturn200AndCorrectValues() throws Exception {
        mockMvc.perform(post("/api/v1/loan/calculate").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "amount": 1200,
                  "annualInterestPercent": 0,
                  "numberOfMonths": 6
                }
                """)).andExpect(status().isOk()).andExpect(jsonPath("$.monthlyPayment").value(200.00)).andExpect(jsonPath("$.totalPayment").value(1200.00));
    }

    @Test
    void calculateLoan_invalidAmount_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/loan/calculate").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "amount": 0,
                  "annualInterestPercent": 5,
                  "numberOfMonths": 10
                }
                """)).andExpect(status().isBadRequest());
    }

    @Test
    void calculateLoan_missingField_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/loan/calculate").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "annualInterestPercent": 5,
                  "numberOfMonths": 10
                }
                """)).andExpect(status().isBadRequest());
    }
}
