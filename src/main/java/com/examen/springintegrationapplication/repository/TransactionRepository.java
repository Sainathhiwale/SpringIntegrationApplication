package com.examen.springintegrationapplication.repository;

import com.examen.springintegrationapplication.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
