package com.testing.piggybank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.testing.piggybank.entities.Account;
import com.testing.piggybank.entities.Transaction;
import com.testing.piggybank.services.TransactionService;

public class TransactionServiceTests {

    private final TransactionService transactionService;

    public TransactionServiceTests() {
        this.transactionService = new TransactionService(null, null, null);
    }
    @Test
    public void filterAndLimitTransactionsTests() {
        final Account account = new Account();
        account.setId(1L);

        final Account accountOther = new Account();
        accountOther.setId(2L);

        final Account accountOtherOther = new Account();
        accountOtherOther.setId(3L);

        List<Transaction> transactions = new ArrayList<>();

        final Transaction transaction = new Transaction();
        transaction.setSenderAccount(account);
        transaction.setReceiverAccount(accountOther);
        transaction.setAmount(new BigDecimal(1000));


        final Transaction transactionOther = new Transaction();
        transaction.setSenderAccount(accountOther);
        transaction.setReceiverAccount(accountOtherOther);
        transaction.setAmount(new BigDecimal(1000));

        transactions.add(transaction);
        transactions.add(transactionOther);

        List<Transaction> results = transactionService.filterAndLimitTransactions(transactions, account.getId(), null);

        Assertions.assertEquals(1, results.size());
    }
}
