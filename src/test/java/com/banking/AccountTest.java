package com.banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Tests for the Account class.
 */
public class AccountTest {
    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account("ACC001", "John Doe", "password123", 1000.0);
    }

    @Test
    public void testAccountCreation() {
        assertEquals("ACC001", account.getAccountNumber());
        assertEquals("John Doe", account.getAccountHolder());
        assertEquals(1000.0, account.getBalance(), 0.01);
    }

    @Test
    public void testPasswordTooShort() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Account("ACC002", "Jane Doe", "123", 500.0);
        });
    }

    @Test
    public void testPasswordVerification() {
        assertTrue(account.verifyPassword("password123"));
        assertFalse(account.verifyPassword("wrongpassword"));
    }

    @Test
    public void testDeposit() {
        assertTrue(account.deposit(500.0, "Test deposit"));
        assertEquals(1500.0, account.getBalance(), 0.01);
    }

    @Test
    public void testDepositNegativeAmount() {
        assertFalse(account.deposit(-500.0, "Invalid deposit"));
        assertEquals(1000.0, account.getBalance(), 0.01);
    }

    @Test
    public void testDepositZero() {
        assertFalse(account.deposit(0.0, "Zero deposit"));
        assertEquals(1000.0, account.getBalance(), 0.01);
    }

    @Test
    public void testWithdraw() {
        assertTrue(account.withdraw(300.0, "Test withdrawal"));
        assertEquals(700.0, account.getBalance(), 0.01);
    }

    @Test
    public void testWithdrawInsufficientBalance() {
        assertFalse(account.withdraw(1500.0, "Too much"));
        assertEquals(1000.0, account.getBalance(), 0.01);
    }

    @Test
    public void testWithdrawNegativeAmount() {
        assertFalse(account.withdraw(-100.0, "Invalid"));
        assertEquals(1000.0, account.getBalance(), 0.01);
    }

    @Test
    public void testWithdrawZero() {
        assertFalse(account.withdraw(0.0, "Zero withdrawal"));
        assertEquals(1000.0, account.getBalance(), 0.01);
    }

    @Test
    public void testGetBalance() {
        assertEquals(1000.0, account.getBalance(), 0.01);
    }

    @Test
    public void testTransactionHistory() {
        account.deposit(500.0, "Test deposit");
        account.withdraw(200.0, "Test withdrawal");

        List<Account.Transaction> history = account.getTransactionHistory(null);
        assertEquals(3, history.size()); // Initial deposit + 2 transactions

        Account.Transaction lastTransaction = history.get(history.size() - 1);
        assertEquals("Test withdrawal", lastTransaction.getDescription());
        assertEquals(200.0, lastTransaction.getAmount(), 0.01);
        assertEquals("debit", lastTransaction.getType());
    }

    @Test
    public void testTransactionHistoryWithLimit() {
        account.deposit(100.0, "Deposit 1");
        account.deposit(200.0, "Deposit 2");
        account.deposit(300.0, "Deposit 3");

        List<Account.Transaction> history = account.getTransactionHistory(2);
        assertEquals(2, history.size());
        assertEquals("Deposit 3", history.get(1).getDescription());
    }
}
