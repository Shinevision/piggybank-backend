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

        final Account accountNoTransactions = new Account();
        accountOtherOther.setId(4L);

        
        final Transaction transaction = new Transaction();
        transaction.setSenderAccount(account);
        transaction.setReceiverAccount(accountOther);
        transaction.setAmount(new BigDecimal(1000));

        final Transaction transactionTwo = new Transaction();
        transactionTwo.setSenderAccount(account);
        transactionTwo.setReceiverAccount(accountOther);
        transactionTwo.setAmount(new BigDecimal(1000));


        final Transaction transactionOther = new Transaction();
        transactionOther.setSenderAccount(accountOther);
        transactionOther.setReceiverAccount(accountOtherOther);
        transactionOther.setAmount(new BigDecimal(1000));
        
        final List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        transactions.add(transactionTwo);
        transactions.add(transactionOther);

        List<Transaction> resultsFirstAccount = transactionService.filterAndLimitTransactions(transactions, account.getId(), 10);
        List<Transaction> resultsFirstAccountLimitedToOne = transactionService.filterAndLimitTransactions(transactions, account.getId(), 1);
        List<Transaction> resultsSecondAccount = transactionService.filterAndLimitTransactions(transactions, accountOtherOther.getId(), 10);
        List<Transaction> resultsEmptyAccount = transactionService.filterAndLimitTransactions(transactions, accountNoTransactions.getId(), 10);

        Assertions.assertEquals(2, resultsFirstAccount.size());
        Assertions.assertEquals(1, resultsFirstAccountLimitedToOne.size());
        Assertions.assertEquals(1, resultsSecondAccount.size());
        Assertions.assertEquals(0, resultsEmptyAccount.size());
    }
}
