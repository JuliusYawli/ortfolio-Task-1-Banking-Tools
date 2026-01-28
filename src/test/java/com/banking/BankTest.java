package com.banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Tests for the Bank class.
 */
public class BankTest {
    @TempDir
    Path tempDir;
    
    private Bank bank;
    private String testFile;

    @BeforeEach
    public void setUp() {
        testFile = tempDir.resolve("test_bank.json").toString();
        bank = new Bank(testFile);
    }

    @Test
    public void testCreateAccount() {
        assertTrue(bank.createAccount("ACC001", "John Doe", "password123", 1000.0));
        assertNotNull(bank.getAccount("ACC001"));
        assertEquals("John Doe", bank.getAccount("ACC001").getAccountHolder());
    }

    @Test
    public void testCreateDuplicateAccount() {
        bank.createAccount("ACC001", "John Doe", "password123", 1000.0);
        assertFalse(bank.createAccount("ACC001", "Jane Doe", "password456", 500.0));
    }

    @Test
    public void testCreateAccountEmptyNumber() {
        assertFalse(bank.createAccount("", "John Doe", "password123", 1000.0));
        assertFalse(bank.createAccount("   ", "John Doe", "password123", 1000.0));
    }

    @Test
    public void testCreateAccountEmptyHolder() {
        assertFalse(bank.createAccount("ACC001", "", "password123", 1000.0));
        assertFalse(bank.createAccount("ACC001", "   ", "password123", 1000.0));
    }

    @Test
    public void testCreateAccountNegativeBalance() {
        assertFalse(bank.createAccount("ACC001", "John Doe", "password123", -100.0));
    }

    @Test
    public void testCreateAccountShortPassword() {
        assertFalse(bank.createAccount("ACC001", "John Doe", "123", 1000.0));
    }

    @Test
    public void testAuthenticate() {
        bank.createAccount("ACC001", "John Doe", "password123", 1000.0);

        // Test correct credentials
        Account account = bank.authenticate("ACC001", "password123");
        assertNotNull(account);

        // Test wrong password
        account = bank.authenticate("ACC001", "wrongpassword");
        assertNull(account);

        // Test non-existent account
        account = bank.authenticate("ACC999", "password123");
        assertNull(account);
    }

    @Test
    public void testTransfer() {
        bank.createAccount("ACC001", "John Doe", "password123", 1000.0);
        bank.createAccount("ACC002", "Jane Doe", "password456", 500.0);

        // Test successful transfer
        Bank.TransferResult result = bank.transfer("ACC001", "ACC002", 300.0, "password123");
        assertTrue(result.isSuccess());
        assertEquals("Transfer successful", result.getMessage());

        Account acc1 = bank.getAccount("ACC001");
        Account acc2 = bank.getAccount("ACC002");
        assertEquals(700.0, acc1.getBalance(), 0.01);
        assertEquals(800.0, acc2.getBalance(), 0.01);
    }

    @Test
    public void testTransferInsufficientBalance() {
        bank.createAccount("ACC001", "John Doe", "password123", 500.0);
        bank.createAccount("ACC002", "Jane Doe", "password456", 500.0);

        Bank.TransferResult result = bank.transfer("ACC001", "ACC002", 1000.0, "password123");
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Insufficient balance"));
    }

    @Test
    public void testTransferWrongPassword() {
        bank.createAccount("ACC001", "John Doe", "password123", 1000.0);
        bank.createAccount("ACC002", "Jane Doe", "password456", 500.0);

        Bank.TransferResult result = bank.transfer("ACC001", "ACC002", 300.0, "wrongpassword");
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Authentication failed"));
    }

    @Test
    public void testTransferToNonExistentAccount() {
        bank.createAccount("ACC001", "John Doe", "password123", 1000.0);

        Bank.TransferResult result = bank.transfer("ACC001", "ACC999", 300.0, "password123");
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Destination account not found"));
    }

    @Test
    public void testTransferToSameAccount() {
        bank.createAccount("ACC001", "John Doe", "password123", 1000.0);

        Bank.TransferResult result = bank.transfer("ACC001", "ACC001", 300.0, "password123");
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Cannot transfer to the same account"));
    }

    @Test
    public void testTransferInvalidAmount() {
        bank.createAccount("ACC001", "John Doe", "password123", 1000.0);
        bank.createAccount("ACC002", "Jane Doe", "password456", 500.0);

        Bank.TransferResult result = bank.transfer("ACC001", "ACC002", 0.0, "password123");
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Invalid amount"));

        result = bank.transfer("ACC001", "ACC002", -100.0, "password123");
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Invalid amount"));
    }

    @Test
    public void testDataPersistence() {
        // Create accounts in first bank instance
        bank.createAccount("ACC001", "John Doe", "password123", 1000.0);
        bank.createAccount("ACC002", "Jane Doe", "password456", 500.0);

        // Create new bank instance (should load data)
        Bank bank2 = new Bank(testFile);
        Account account = bank2.getAccount("ACC001");

        assertNotNull(account);
        assertEquals("John Doe", account.getAccountHolder());
        assertEquals(1000.0, account.getBalance(), 0.01);
    }

    @Test
    public void testListAccounts() {
        bank.createAccount("ACC001", "John Doe", "password123", 1000.0);
        bank.createAccount("ACC002", "Jane Doe", "password456", 500.0);

        List<Map<String, Object>> accounts = bank.listAccounts();
        assertEquals(2, accounts.size());

        boolean foundAcc1 = false;
        boolean foundAcc2 = false;
        for (Map<String, Object> acc : accounts) {
            if ("ACC001".equals(acc.get("account_number"))) {
                foundAcc1 = true;
            }
            if ("ACC002".equals(acc.get("account_number"))) {
                foundAcc2 = true;
            }
        }
        assertTrue(foundAcc1);
        assertTrue(foundAcc2);
    }
}
