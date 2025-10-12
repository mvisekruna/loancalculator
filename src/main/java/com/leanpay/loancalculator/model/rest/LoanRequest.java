package com.leanpay.loancalculator.model.rest;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Data
public class LoanRequest {

    @NotNull(message = "Amount is required.")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0.")
    private BigDecimal amount;

    @NotNull(message = "Annual interest percent is required.")
    @DecimalMin(value = "0.0", message = "Interest percent cannot be negative.")
    @DecimalMax(value = "100.0", message = "Interest percent cannot exceed 100.")
    private BigDecimal annualInterestPercent;

    @Min(value = 1, message = "Number of months must be at least 1.")
    private int numberOfMonths;

}
