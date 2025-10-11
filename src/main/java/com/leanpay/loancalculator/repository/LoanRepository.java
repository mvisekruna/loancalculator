package com.leanpay.loancalculator.repository;

import com.leanpay.loancalculator.model.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
}
