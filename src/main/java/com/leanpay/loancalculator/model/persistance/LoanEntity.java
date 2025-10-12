package com.leanpay.loancalculator.model.persistance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Data
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @Column(nullable = false)
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private BigDecimal annualInterestPercent;

    @Column(nullable = false)
    @Min(1)
    private int numberOfMonths;

    private BigDecimal monthlyPayment;

    private BigDecimal totalPayment;

    private LocalDateTime createdAt;

}
