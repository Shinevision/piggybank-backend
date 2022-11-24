package com.testing.piggybank.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.testing.piggybank.entities.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
