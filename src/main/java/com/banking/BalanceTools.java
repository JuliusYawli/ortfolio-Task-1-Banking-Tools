package com.banking;

/**
 * Tools for banking balance calculations.
 */
public class BalanceTools {

    /**
     * Calculate the new balance after a deposit.
     * 
     * @param existingBalance The current balance in pence
     * @param depositAmount The amount being deposited in pence
     * @return The new balance in pence
     */
    public static int newBalance(int existingBalance, int depositAmount) {
        return existingBalance + depositAmount;
    }

    /**
     * Check if there are sufficient funds available for a transaction.
     * 
     * @param currentBalance The current balance in pence
     * @param transactionAmount The transaction amount in pence
     * @return true if funds are available, false otherwise
     */
    public static boolean hasAvailableFunds(int currentBalance, int transactionAmount) {
        return currentBalance >= transactionAmount;
    }

    /**
     * Calculate the annual interest on a savings bond.
     * 
     * @param openingBalance The opening balance in pence
     * @param interestRate The annual interest rate (e.g., 0.05 for 5%)
     * @return The interest amount in pence
     */
    public static int annualBondInterest(int openingBalance, double interestRate) {
        return (int) (openingBalance * interestRate);
    }

    /**
     * Calculate the expected balance after one year with interest.
     * 
     * @param startingBalance The starting balance in pence
     * @param interestRate The annual interest rate (e.g., 0.05 for 5%)
     * @return The expected balance at the end of the year in pence
     */
    public static int bondIllustration(int startingBalance, double interestRate) {
        return startingBalance + annualBondInterest(startingBalance, interestRate);
    }
}
