package com.banking;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bank account with basic operations.
 */
public class Account {
    private String accountNumber;
    private String accountHolder;
    private String passwordHash;
    private double balance;
    private List<Transaction> transactions;
    private String createdAt;

    /**
     * Create a new bank account.
     * 
     * @param accountNumber Unique account identifier
     * @param accountHolder Name of the account holder
     * @param password Account password (will be hashed)
     * @param initialBalance Starting balance
     * @throws IllegalArgumentException if password is too short
     */
    public Account(String accountNumber, String accountHolder, String password, double initialBalance) {
        if (password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters long");
        }

        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.passwordHash = hashPassword(password);
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        if (initialBalance > 0) {
            addTransaction("Initial Deposit", initialBalance, "credit");
        }
    }

    /**
     * Private constructor for deserialization.
     */
    private Account() {
        this.transactions = new ArrayList<>();
    }

    /**
     * Hash password using SHA-256.
     * 
     * @param password Plain text password
     * @return Hashed password
     */
    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * Verify if the provided password is correct.
     * 
     * @param password Password to verify
     * @return true if password matches, false otherwise
     */
    public boolean verifyPassword(String password) {
        return this.passwordHash.equals(hashPassword(password));
    }

    /**
     * Add a transaction to the account history.
     * 
     * @param description Transaction description
     * @param amount Transaction amount
     * @param type Transaction type (credit or debit)
     */
    private void addTransaction(String description, double amount, String type) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Transaction transaction = new Transaction(date, description, amount, type, this.balance);
        this.transactions.add(transaction);
    }

    /**
     * Deposit money into the account.
     * 
     * @param amount Amount to deposit
     * @param description Transaction description
     * @return true if successful, false otherwise
     */
    public boolean deposit(double amount, String description) {
        if (amount <= 0) {
            return false;
        }

        this.balance += amount;
        addTransaction(description, amount, "credit");
        return true;
    }

    /**
     * Withdraw money from the account.
     * 
     * @param amount Amount to withdraw
     * @param description Transaction description
     * @return true if successful, false otherwise
     */
    public boolean withdraw(double amount, String description) {
        if (amount <= 0 || amount > this.balance) {
            return false;
        }

        this.balance -= amount;
        addTransaction(description, amount, "debit");
        return true;
    }

    /**
     * Get current account balance.
     * 
     * @return Current balance
     */
    public double getBalance() {
        return this.balance;
    }

    /**
     * Get transaction history.
     * 
     * @param limit Maximum number of transactions to return (null for all)
     * @return List of transactions
     */
    public List<Transaction> getTransactionHistory(Integer limit) {
        if (limit != null && limit > 0) {
            int size = transactions.size();
            int fromIndex = Math.max(0, size - limit);
            return new ArrayList<>(transactions.subList(fromIndex, size));
        }
        return new ArrayList<>(transactions);
    }

    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // Setters for deserialization
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Inner class representing a transaction.
     */
    public static class Transaction {
        private String date;
        private String description;
        private double amount;
        private String type;
        private double balanceAfter;

        public Transaction(String date, String description, double amount, String type, double balanceAfter) {
            this.date = date;
            this.description = description;
            this.amount = amount;
            this.type = type;
            this.balanceAfter = balanceAfter;
        }

        // Getters
        public String getDate() {
            return date;
        }

        public String getDescription() {
            return description;
        }

        public double getAmount() {
            return amount;
        }

        public String getType() {
            return type;
        }

        public double getBalanceAfter() {
            return balanceAfter;
        }

        // Setters for Gson
        public void setDate(String date) {
            this.date = date;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setBalanceAfter(double balanceAfter) {
            this.balanceAfter = balanceAfter;
        }
    }
}
