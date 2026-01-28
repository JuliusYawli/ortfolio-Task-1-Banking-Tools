package com.banking;

import java.util.List;
import java.util.Map;

/**
 * Demo script to test the banking application.
 */
public class Demo {
    public static void main(String[] args) {
        System.out.println("🏦 Banking Tools - Java Demo\n");

        // Create a bank instance
        Bank bank = new Bank("demo_bank.json");

        // Create some accounts
        System.out.println("1. Creating accounts...");
        bank.createAccount("ACC001", "John Doe", "password123", 1000.0);
        bank.createAccount("ACC002", "Jane Smith", "password456", 500.0);
        System.out.println("   ✅ Created ACC001 (John Doe) with $1000");
        System.out.println("   ✅ Created ACC002 (Jane Smith) with $500\n");

        // Test authentication
        System.out.println("2. Testing authentication...");
        Account acc1 = bank.authenticate("ACC001", "password123");
        if (acc1 != null) {
            System.out.println("   ✅ Successfully authenticated " + acc1.getAccountHolder() + "\n");
        }

        // Test deposit
        System.out.println("3. Testing deposit...");
        if (acc1.deposit(250.0, "Test deposit")) {
            bank.saveData();
            System.out.printf("   ✅ Deposited $250 to ACC001\n");
            System.out.printf("   💰 New balance: $%.2f\n\n", acc1.getBalance());
        }

        // Test withdrawal
        System.out.println("4. Testing withdrawal...");
        if (acc1.withdraw(100.0, "Test withdrawal")) {
            bank.saveData();
            System.out.printf("   ✅ Withdrew $100 from ACC001\n");
            System.out.printf("   💰 New balance: $%.2f\n\n", acc1.getBalance());
        }

        // Test transfer
        System.out.println("5. Testing transfer...");
        Bank.TransferResult result = bank.transfer("ACC001", "ACC002", 200.0, "password123");
        if (result.isSuccess()) {
            System.out.printf("   ✅ Transferred $200 from ACC001 to ACC002\n");
            System.out.printf("   💰 ACC001 balance: $%.2f\n", acc1.getBalance());
            Account acc2 = bank.getAccount("ACC002");
            System.out.printf("   💰 ACC002 balance: $%.2f\n\n", acc2.getBalance());
        }

        // Show transaction history
        System.out.println("6. Transaction history for ACC001:");
        List<Account.Transaction> transactions = acc1.getTransactionHistory(3);
        for (Account.Transaction trans : transactions) {
            System.out.printf("   - %s: $%.2f (%s)\n", 
                trans.getDescription(), trans.getAmount(), trans.getType());
        }

        System.out.println("\n7. Listing all accounts:");
        List<Map<String, Object>> accounts = bank.listAccounts();
        for (Map<String, Object> acc : accounts) {
            System.out.printf("   - %s: %s - $%.2f\n",
                acc.get("account_number"),
                acc.get("account_holder"),
                acc.get("balance"));
        }

        System.out.println("\n✅ Demo completed successfully!");

        // Clean up
        java.io.File demoFile = new java.io.File("demo_bank.json");
        if (demoFile.exists()) {
            demoFile.delete();
        }
    }
}
