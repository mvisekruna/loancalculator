package com.leanpay.loancalculator.controller;

import com.leanpay.loancalculator.model.LoanRequest;
import com.leanpay.loancalculator.model.LoanResponse;
import com.leanpay.loancalculator.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loan")
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/calculate")
    public ResponseEntity<LoanResponse> calculateLoan(@RequestBody LoanRequest loanRequest) {
        return ResponseEntity.ok(loanService.calculateLoan(loanRequest));
    }

}
