package com.banking;

import java.io.Console;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Command-line interface for banking operations.
 */
public class BankingCLI {
    private Bank bank;
    private Account currentAccount;
    private Scanner scanner;

    public BankingCLI() {
        this.bank = new Bank("bank_data.json");
        this.currentAccount = null;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Display main menu.
     */
    private void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(" ".repeat(15) + "BANKING TOOLS");
        System.out.println("=".repeat(50));

        if (currentAccount != null) {
            System.out.println("\nLogged in as: " + currentAccount.getAccountHolder());
            System.out.println("Account: " + currentAccount.getAccountNumber());
            System.out.printf("Balance: $%.2f\n", currentAccount.getBalance());
            System.out.println("\n1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. View Transaction History");
            System.out.println("6. Logout");
            System.out.println("7. Exit");
        } else {
            System.out.println("\n1. Create New Account");
            System.out.println("2. Login to Existing Account");
            System.out.println("3. View All Accounts (Admin)");
            System.out.println("4. Exit");
        }

        System.out.println("=".repeat(50));
    }

    /**
     * Create a new bank account.
     */
    private void createAccount() {
        System.out.println("\n--- Create New Account ---");
        
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine().trim();

        if (accountNumber.isEmpty()) {
            System.out.println("❌ Account number cannot be empty!");
            return;
        }

        if (bank.getAccount(accountNumber) != null) {
            System.out.println("❌ Account already exists!");
            return;
        }

        System.out.print("Enter account holder name: ");
        String accountHolder = scanner.nextLine().trim();

        if (accountHolder.isEmpty()) {
            System.out.println("❌ Name cannot be empty!");
            return;
        }

        String password = readPassword("Enter password (min 4 characters): ");
        
        if (password.length() < 4) {
            System.out.println("❌ Password must be at least 4 characters long!");
            return;
        }

        String passwordConfirm = readPassword("Confirm password: ");

        if (!password.equals(passwordConfirm)) {
            System.out.println("❌ Passwords do not match!");
            return;
        }

        double initialBalance;
        try {
            System.out.print("Enter initial balance (0 or more): $");
            initialBalance = Double.parseDouble(scanner.nextLine());
            if (initialBalance < 0) {
                System.out.println("❌ Initial balance cannot be negative!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid amount!");
            return;
        }

        if (bank.createAccount(accountNumber, accountHolder, password, initialBalance)) {
            System.out.println("\n✅ Account created successfully!");
            System.out.println("Account Number: " + accountNumber);
            System.out.println("Account Holder: " + accountHolder);
            System.out.printf("Initial Balance: $%.2f\n", initialBalance);
        } else {
            System.out.println("❌ Failed to create account!");
        }
    }

    /**
     * Login to an existing account.
     */
    private void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine().trim();

        String password = readPassword("Enter password: ");

        Account account = bank.authenticate(accountNumber, password);
        if (account != null) {
            currentAccount = account;
            System.out.println("\n✅ Welcome, " + account.getAccountHolder() + "!");
        } else {
            System.out.println("❌ Invalid account number or password!");
        }
    }

    /**
     * Check current balance.
     */
    private void checkBalance() {
        if (currentAccount == null) {
            System.out.println("❌ Please login first!");
            return;
        }

        System.out.printf("\n💰 Current Balance: $%.2f\n", currentAccount.getBalance());
    }

    /**
     * Deposit money into account.
     */
    private void deposit() {
        if (currentAccount == null) {
            System.out.println("❌ Please login first!");
            return;
        }

        System.out.println("\n--- Deposit Money ---");
        try {
            System.out.print("Enter amount to deposit: $");
            double amount = Double.parseDouble(scanner.nextLine());

            System.out.print("Description (optional): ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                description = "Deposit";
            }

            if (currentAccount.deposit(amount, description)) {
                bank.saveData();
                System.out.printf("\n✅ Deposited $%.2f successfully!\n", amount);
                System.out.printf("💰 New Balance: $%.2f\n", currentAccount.getBalance());
            } else {
                System.out.println("❌ Invalid deposit amount!");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid amount!");
        }
    }

    /**
     * Withdraw money from account.
     */
    private void withdraw() {
        if (currentAccount == null) {
            System.out.println("❌ Please login first!");
            return;
        }

        System.out.println("\n--- Withdraw Money ---");
        try {
            System.out.print("Enter amount to withdraw: $");
            double amount = Double.parseDouble(scanner.nextLine());

            System.out.print("Description (optional): ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                description = "Withdrawal";
            }

            if (currentAccount.withdraw(amount, description)) {
                bank.saveData();
                System.out.printf("\n✅ Withdrew $%.2f successfully!\n", amount);
                System.out.printf("💰 New Balance: $%.2f\n", currentAccount.getBalance());
            } else {
                System.out.println("❌ Invalid amount or insufficient balance!");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid amount!");
        }
    }

    /**
     * Transfer money to another account.
     */
    private void transfer() {
        if (currentAccount == null) {
            System.out.println("❌ Please login first!");
            return;
        }

        System.out.println("\n--- Transfer Money ---");
        System.out.print("Enter destination account number: ");
        String toAccount = scanner.nextLine().trim();

        try {
            System.out.print("Enter amount to transfer: $");
            double amount = Double.parseDouble(scanner.nextLine());

            String password = readPassword("Enter your password to confirm: ");

            Bank.TransferResult result = bank.transfer(
                currentAccount.getAccountNumber(),
                toAccount,
                amount,
                password
            );

            if (result.isSuccess()) {
                System.out.println("\n✅ " + result.getMessage());
                System.out.printf("💰 New Balance: $%.2f\n", currentAccount.getBalance());
            } else {
                System.out.println("❌ " + result.getMessage());
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid amount!");
        }
    }

    /**
     * View transaction history.
     */
    private void viewTransactions() {
        if (currentAccount == null) {
            System.out.println("❌ Please login first!");
            return;
        }

        System.out.println("\n--- Transaction History ---");
        List<Account.Transaction> transactions = currentAccount.getTransactionHistory(null);

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.printf("\n%-20s %-25s %-10s %-12s %s\n", 
            "Date", "Description", "Type", "Amount", "Balance");
        System.out.println("-".repeat(90));

        for (Account.Transaction trans : transactions) {
            String date = trans.getDate().substring(0, Math.min(19, trans.getDate().length()));
            String desc = trans.getDescription();
            if (desc.length() > 24) {
                desc = desc.substring(0, 24);
            }
            String type = trans.getType().toUpperCase();
            String amount = String.format("$%.2f", trans.getAmount());
            String balance = String.format("$%.2f", trans.getBalanceAfter());

            System.out.printf("%-20s %-25s %-10s %-12s %s\n", 
                date, desc, type, amount, balance);
        }
    }

    /**
     * View all accounts (admin feature - requires simple password).
     */
    private void viewAllAccounts() {
        System.out.println("\n--- All Accounts (Admin View) ---");
        System.out.print("Admin password: ");
        String adminPassword = scanner.nextLine().trim();
        
        // Simple admin check - in production, use proper authentication
        if (!"admin123".equals(adminPassword)) {
            System.out.println("❌ Invalid admin password!");
            return;
        }
        
        List<Map<String, Object>> accounts = bank.listAccounts();

        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        System.out.printf("\n%-20s %-30s %s\n", "Account Number", "Account Holder", "Balance");
        System.out.println("-".repeat(70));

        for (Map<String, Object> acc : accounts) {
            System.out.printf("%-20s %-30s $%.2f\n",
                acc.get("account_number"),
                acc.get("account_holder"),
                acc.get("balance"));
        }
    }

    /**
     * Logout from current account.
     */
    private void logout() {
        if (currentAccount != null) {
            System.out.println("\n👋 Goodbye, " + currentAccount.getAccountHolder() + "!");
            currentAccount = null;
        } else {
            System.out.println("❌ Not logged in!");
        }
    }

    /**
     * Read password from console (with masking if available).
     */
    private String readPassword(String prompt) {
        Console console = System.console();
        if (console != null) {
            char[] passwordChars = console.readPassword(prompt);
            return new String(passwordChars);
        } else {
            // Fallback for environments without console
            System.out.print(prompt);
            return scanner.nextLine();
        }
    }

    /**
     * Run the main application loop.
     */
    public void run() {
        System.out.println("\n✨ Welcome to Banking Tools! ✨");

        while (true) {
            displayMenu();
            System.out.print("\nEnter your choice: ");
            String choice = scanner.nextLine().trim();

            if (currentAccount != null) {
                // Logged in menu
                switch (choice) {
                    case "1" -> checkBalance();
                    case "2" -> deposit();
                    case "3" -> withdraw();
                    case "4" -> transfer();
                    case "5" -> viewTransactions();
                    case "6" -> logout();
                    case "7" -> {
                        System.out.println("\n👋 Thank you for using Banking Tools!");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("❌ Invalid choice! Please try again.");
                }
            } else {
                // Not logged in menu
                switch (choice) {
                    case "1" -> createAccount();
                    case "2" -> login();
                    case "3" -> viewAllAccounts();
                    case "4" -> {
                        System.out.println("\n👋 Thank you for using Banking Tools!");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("❌ Invalid choice! Please try again.");
                }
            }

            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    /**
     * Main entry point.
     */
    public static void main(String[] args) {
        try {
            BankingCLI cli = new BankingCLI();
            cli.run();
        } catch (Exception e) {
            System.err.println("\n❌ An error occurred: " + e.getMessage());
            System.exit(1);
        }
    }
}
