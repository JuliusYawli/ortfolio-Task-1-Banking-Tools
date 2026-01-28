package com.banking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main bank class managing multiple accounts.
 */
public class Bank {
    private String dataFile;
    private Map<String, Account> accounts;
    private Gson gson;

    /**
     * Initialize the bank.
     * 
     * @param dataFile Path to the data persistence file
     */
    public Bank(String dataFile) {
        this.dataFile = dataFile;
        this.accounts = new HashMap<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadData();
    }

    /**
     * Load account data from file.
     */
    private void loadData() {
        if (Files.exists(Paths.get(dataFile))) {
            try (FileReader reader = new FileReader(dataFile)) {
                Type listType = new TypeToken<ArrayList<Account>>(){}.getType();
                List<Account> accountList = gson.fromJson(reader, listType);
                if (accountList != null) {
                    for (Account account : accountList) {
                        accounts.put(account.getAccountNumber(), account);
                    }
                }
            } catch (IOException e) {
                // If file is corrupted, start fresh
                accounts = new HashMap<>();
            }
        }
    }

    /**
     * Save account data to file.
     */
    public void saveData() {
        try (FileWriter writer = new FileWriter(dataFile)) {
            List<Account> accountList = new ArrayList<>(accounts.values());
            gson.toJson(accountList, writer);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    /**
     * Create a new bank account.
     * 
     * @param accountNumber Unique account identifier
     * @param accountHolder Name of the account holder
     * @param password Account password
     * @param initialBalance Starting balance
     * @return true if account created successfully, false otherwise
     */
    public boolean createAccount(String accountNumber, String accountHolder, String password, double initialBalance) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }

        if (accountHolder == null || accountHolder.trim().isEmpty()) {
            return false;
        }

        if (accounts.containsKey(accountNumber)) {
            return false;
        }

        if (initialBalance < 0) {
            return false;
        }

        try {
            Account account = new Account(accountNumber, accountHolder, password, initialBalance);
            accounts.put(accountNumber, account);
            saveData();
            return true;
        } catch (IllegalArgumentException e) {
            // Password validation failed
            return false;
        }
    }

    /**
     * Authenticate and return account.
     * 
     * @param accountNumber Account identifier
     * @param password Account password
     * @return Account object if authentication successful, null otherwise
     */
    public Account authenticate(String accountNumber, String password) {
        Account account = accounts.get(accountNumber);
        if (account != null && account.verifyPassword(password)) {
            return account;
        }
        return null;
    }

    /**
     * Get account by account number (without authentication).
     * 
     * @param accountNumber Account identifier
     * @return Account object or null
     */
    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    /**
     * Transfer money between accounts.
     * 
     * @param fromAccount Source account number
     * @param toAccount Destination account number
     * @param amount Amount to transfer
     * @param password Source account password
     * @return TransferResult with success status and message
     */
    public TransferResult transfer(String fromAccount, String toAccount, double amount, String password) {
        // Prevent self-transfer
        if (fromAccount.equals(toAccount)) {
            return new TransferResult(false, "Cannot transfer to the same account");
        }

        // Authenticate source account
        Account source = authenticate(fromAccount, password);
        if (source == null) {
            return new TransferResult(false, "Authentication failed");
        }

        // Check if destination account exists
        Account destination = getAccount(toAccount);
        if (destination == null) {
            return new TransferResult(false, "Destination account not found");
        }

        // Check if source has sufficient balance
        if (amount <= 0) {
            return new TransferResult(false, "Invalid amount");
        }

        if (source.getBalance() < amount) {
            return new TransferResult(false, "Insufficient balance");
        }

        // Perform transfer
        source.withdraw(amount, "Transfer to " + toAccount);
        destination.deposit(amount, "Transfer from " + fromAccount);
        saveData();

        return new TransferResult(true, "Transfer successful");
    }

    /**
     * Get list of all accounts (basic info only).
     * 
     * @return List of account information maps
     */
    public List<Map<String, Object>> listAccounts() {
        List<Map<String, Object>> accountList = new ArrayList<>();
        for (Account account : accounts.values()) {
            Map<String, Object> info = new HashMap<>();
            info.put("account_number", account.getAccountNumber());
            info.put("account_holder", account.getAccountHolder());
            info.put("balance", account.getBalance());
            accountList.add(info);
        }
        return accountList;
    }

    /**
     * Result class for transfer operations.
     */
    public static class TransferResult {
        private boolean success;
        private String message;

        public TransferResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
