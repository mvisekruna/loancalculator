package com.leanpay.loancalculator.service;

import com.leanpay.loancalculator.model.rest.LoanRequest;
import com.leanpay.loancalculator.model.rest.LoanResponse;

public interface LoanService {
    LoanResponse calculateLoan(LoanRequest loanRequest);
}

