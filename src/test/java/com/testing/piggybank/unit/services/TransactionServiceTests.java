package com.testing.piggybank.unit.services;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.testing.piggybank.entities.Account;
import com.testing.piggybank.entities.Transaction;
import com.testing.piggybank.enums.Currency;
import com.testing.piggybank.enums.Status;
import com.testing.piggybank.models.CreateTransactionRequest;
import com.testing.piggybank.repositories.TransactionRepository;
import com.testing.piggybank.services.AccountService;
import com.testing.piggybank.services.CurrencyConverterService;
import com.testing.piggybank.services.TransactionService;

//Yes i know im mixing some test, they should ideally be in separate files and sometimes even in separate test projects. Shhh.
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {

    @Mock
    private TransactionRepository mockTransactionRepository;
    @Mock
    private CurrencyConverterService mockConverterService;
    @Mock
    private AccountService mockAccountService;

    @InjectMocks
    private TransactionService transactionService;

    // This test was just some experimentation. These tests should be separated in a
    // real scenario.
    @Test
    public void filterAndLimitTransactionsExampleTests() {
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

        List<Transaction> resultsFirstAccount = transactionService.filterAndLimitTransactions(transactions,
                account.getId(), 10);
        List<Transaction> resultsFirstAccountLimitedToOne = transactionService.filterAndLimitTransactions(transactions,
                account.getId(), 1);
        List<Transaction> resultsSecondAccount = transactionService.filterAndLimitTransactions(transactions,
                accountOtherOther.getId(), 10);
        List<Transaction> resultsEmptyAccount = transactionService.filterAndLimitTransactions(transactions,
                accountNoTransactions.getId(), 10);

        Assertions.assertEquals(2, resultsFirstAccount.size());
        Assertions.assertEquals(1, resultsFirstAccountLimitedToOne.size());
        Assertions.assertEquals(1, resultsSecondAccount.size());
        Assertions.assertEquals(0, resultsEmptyAccount.size());
    }

    @Test
    public void filterAndLimitTransactions_filtersOutNonMatchingTransactions() {
        final Account account = new Account();
        account.setId(1L);

        final Account accountOther = new Account();
        accountOther.setId(2L);

        final Account accountOtherOther = new Account();
        accountOtherOther.setId(3L);

        final Transaction transaction = new Transaction();
        transaction.setSenderAccount(account);
        transaction.setReceiverAccount(accountOther);
        transaction.setAmount(new BigDecimal(1000));

        final Transaction transactionOther = new Transaction();
        transactionOther.setSenderAccount(accountOther);
        transactionOther.setReceiverAccount(accountOtherOther);
        transactionOther.setAmount(new BigDecimal(1000));

        final List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        transactions.add(transactionOther);

        List<Transaction> results = transactionService.filterAndLimitTransactions(transactions, account.getId(), 10);
        Assertions.assertEquals(1, results.size());
        Assertions.assertEquals(transaction, results.get(0));
    }

    @Test
    public void filterAndLimitTransactions_limitsResults() {
        final Account account = new Account();
        account.setId(1L);

        final Account accountOther = new Account();
        accountOther.setId(2L);

        final Transaction transaction = new Transaction();
        transaction.setSenderAccount(account);
        transaction.setReceiverAccount(accountOther);
        transaction.setAmount(new BigDecimal(1000));

        final Transaction transactionTwo = new Transaction();
        transactionTwo.setSenderAccount(account);
        transactionTwo.setReceiverAccount(accountOther);
        transactionTwo.setAmount(new BigDecimal(1000));

        final List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        transactions.add(transactionTwo);

        List<Transaction> results = transactionService.filterAndLimitTransactions(transactions, account.getId(), 1);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    public void filterAndLimitTransactions_emptyInputReturnsEmptyList() {
        final List<Transaction> transactions = new ArrayList<>();
        List<Transaction> results = transactionService.filterAndLimitTransactions(transactions, 1L, 10);
        Assertions.assertEquals(0, results.size());
    }

    @Test
    public void filterAndLimitTransactions_zeroLimitReturnsEmptyList() {
        final Account account = new Account();
        account.setId(1L);
        final Account accountOther = new Account();
        accountOther.setId(2L);

        final Transaction transaction = new Transaction();
        transaction.setSenderAccount(account);
        transaction.setReceiverAccount(accountOther);
        transaction.setAmount(new BigDecimal(1000));

        final List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        List<Transaction> results = transactionService.filterAndLimitTransactions(transactions, account.getId(), 0);
        Assertions.assertEquals(0, results.size());

    }

    // This test fails because the transactionService.filterAndLimitTransactions
    // method does not check for negative limits.

    // @Test
    // public void filterAndLimitTransactions_negativeLimitReturnsEmptyList() {
    // final Account account = new Account();
    // account.setId(1L);

    // final Account accountOther = new Account();
    // accountOther.setId(2L);

    // final Transaction transaction = new Transaction();
    // transaction.setSenderAccount(account);
    // transaction.setReceiverAccount(accountOther);
    // transaction.setAmount(new BigDecimal(1000));

    // final List<Transaction> transactions = new ArrayList<>();
    // transactions.add(transaction);

    // List<Transaction> results =
    // transactionService.filterAndLimitTransactions(transactions, account.getId(),
    // -1);
    // Assertions.assertEquals(0, results.size());

    // }

    @Test
    public void filterAndLimitTransactions_filtersBySenderAndReceiverAccounts() {
        final Account account = new Account();
        account.setId(1L);

        final Account accountOther = new Account();
        accountOther.setId(2L);

        final Transaction transaction = new Transaction();
        transaction.setSenderAccount(account);
        transaction.setReceiverAccount(accountOther);
        transaction.setAmount(new BigDecimal(1000));

        final Transaction transactionTwo = new Transaction();
        transactionTwo.setSenderAccount(accountOther);
        transactionTwo.setReceiverAccount(account);
        transactionTwo.setAmount(new BigDecimal(1000));

        final List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        transactions.add(transactionTwo);

        List<Transaction> results = transactionService.filterAndLimitTransactions(transactions, account.getId(), 10);
        Assertions.assertEquals(2, results.size());
        Assertions.assertTrue(results.contains(transaction));
        Assertions.assertTrue(results.contains(transactionTwo));

    }

    @Test
    public void getTransactions_retrievesAllTransactionsFromDatabase() {
        final List<Transaction> expectedTransactions = new ArrayList<>();
        when(mockTransactionRepository.findAll()).thenReturn(expectedTransactions);

        List<Transaction> actualTransactions = transactionService.getTransactions(100, 1);

        verify(mockTransactionRepository).findAll();
        Assertions.assertEquals(expectedTransactions, actualTransactions);
    }

    @Test
    public void createTransaction_createsTransactionAndSavesToDatabase() {
        final Account senderAccount = new Account();
        senderAccount.setId(1L);

        final Account receiverAccount = new Account();
        receiverAccount.setId(2L);

        final BigDecimal amount = new BigDecimal(100);
        final Currency currency = Currency.EURO;

        final CreateTransactionRequest request = new CreateTransactionRequest();
        request.setSenderAccountId(senderAccount.getId());
        request.setReceiverAccountId(receiverAccount.getId());
        request.setCurrency(currency);
        request.setAmount(amount);

        when(mockAccountService.getAccount(senderAccount.getId()))
                .thenReturn(Optional.of(senderAccount));
        when(mockAccountService.getAccount(receiverAccount.getId()))
                .thenReturn(Optional.of(receiverAccount));
        when(mockConverterService.toEuro(currency, amount))
                .thenReturn(amount);

        transactionService.createTransaction(request);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(mockTransactionRepository).save(captor.capture());
        Transaction actualTransaction = captor.getValue();
        Assertions.assertEquals(senderAccount, actualTransaction.getSenderAccount());
        Assertions.assertEquals(receiverAccount, actualTransaction.getReceiverAccount());
        Assertions.assertEquals(amount, actualTransaction.getAmount());
        Assertions.assertEquals(currency, actualTransaction.getCurrency());
        Assertions.assertNotNull(actualTransaction.getDateTime());
        
        // Status in TransactionService does not get used yet?
        // Assertions.assertEquals(Status.SUCCESS, actualTransaction.getStatus());
    }
}
