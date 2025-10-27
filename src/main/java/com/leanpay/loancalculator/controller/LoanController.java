package com.leanpay.loancalculator.controller;

import com.leanpay.loancalculator.model.rest.LoanRequest;
import com.leanpay.loancalculator.model.rest.LoanResponse;
import com.leanpay.loancalculator.service.implementation.LoanServiceImplementation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loan")
public class LoanController {

    private final LoanServiceImplementation loanServiceImplementation;

    @PostMapping("/calculate")
    public ResponseEntity<LoanResponse> calculateLoan(@Valid @RequestBody LoanRequest loanRequest) {
        return ResponseEntity.ok(loanServiceImplementation.calculateLoan(loanRequest));
    }

}
