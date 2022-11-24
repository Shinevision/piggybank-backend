package com.testing.piggybank.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.testing.piggybank.entities.Account;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    /**
     * Get all accounts from database.
     * @return List of {@link Account}
     */
    List<Account> findAll();

    /**
     * Get all accounts from database for specifiv userId;
     * @return List of {@link Account}
     */
    List<Account> findAllByUserId(long userId);
}
