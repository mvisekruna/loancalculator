package com.leanpay.loancalculator.repository;

import com.leanpay.loancalculator.model.persistance.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanRepository extends JpaRepository<LoanEntity, Long> {

    Optional<LoanEntity> findByRequestHash(String requestHash);

}
