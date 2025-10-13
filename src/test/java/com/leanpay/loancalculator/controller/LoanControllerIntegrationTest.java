package com.leanpay.loancalculator.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.leanpay.loancalculator.util.TestConstants;

@SpringBootTest
@AutoConfigureMockMvc
class LoanControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void calculateLoan_validRequest_shouldReturn200AndCorrectValues() throws Exception {
        mockMvc.perform(post("/api/v1/loan/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestConstants.VALID_REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthlyPayment").value(102.31))
                .andExpect(jsonPath("$.totalPayment").value(1023.10));
    }

    @Test
    void calculateLoan_zeroInterest_shouldReturn200AndCorrectValues() throws Exception {
        mockMvc.perform(post("/api/v1/loan/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestConstants.ZERO_INTEREST_REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthlyPayment").value(200.00))
                .andExpect(jsonPath("$.totalPayment").value(1200.00));
    }

    @Test
    void calculateLoan_invalidAmount_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/loan/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestConstants.INVALID_AMOUNT_REQUEST))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateLoan_missingField_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/loan/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestConstants.MISSING_FIELD_REQUEST))
                .andExpect(status().isBadRequest());
    }
}
