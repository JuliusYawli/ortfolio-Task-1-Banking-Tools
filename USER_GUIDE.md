# Banking Tools - User Guide

## Table of Contents
1. [Getting Started](#getting-started)
2. [Creating an Account](#creating-an-account)
3. [Logging In](#logging-in)
4. [Managing Your Account](#managing-your-account)
5. [Troubleshooting](#troubleshooting)

## Getting Started

### Prerequisites
- Python 3.7 or higher
- No external dependencies required

### Installation
```bash
git clone https://github.com/JuliusYawli/ortfolio-Task-1-Banking-Tools.git
cd ortfolio-Task-1-Banking-Tools
./setup.sh  # Run tests and verify installation
```

### Running the Application
```bash
python3 main.py
```

## Creating an Account

1. From the main menu, select option `1` (Create New Account)
2. Enter a unique account number (e.g., ACC001, USER123)
3. Enter your full name
4. Create a secure password
5. Confirm your password
6. Enter an initial balance (must be 0 or greater)

**Example:**
```
Enter your choice: 1

--- Create New Account ---
Enter account number: ACC001
Enter account holder name: John Doe
Enter password: ****
Confirm password: ****
Enter initial balance (0 or more): $1000

✅ Account created successfully!
Account Number: ACC001
Account Holder: John Doe
Initial Balance: $1000.00
```

## Logging In

1. From the main menu, select option `2` (Login to Existing Account)
2. Enter your account number
3. Enter your password

**Example:**
```
Enter your choice: 2

--- Login ---
Enter account number: ACC001
Enter password: ****

✅ Welcome, John Doe!
```

## Managing Your Account

Once logged in, you have access to the following features:

### 1. Check Balance
View your current account balance at any time.

### 2. Deposit Money
Add funds to your account:
- Enter the amount to deposit
- Optionally add a description
- Funds are immediately available

**Example:**
```
Enter your choice: 2

--- Deposit Money ---
Enter amount to deposit: $500
Description (optional): Salary payment

✅ Deposited $500.00 successfully!
💰 New Balance: $1500.00
```

### 3. Withdraw Money
Take money from your account:
- Enter the amount to withdraw
- Must have sufficient balance
- Optionally add a description

**Example:**
```
Enter your choice: 3

--- Withdraw Money ---
Enter amount to withdraw: $200
Description (optional): ATM withdrawal

✅ Withdrew $200.00 successfully!
💰 New Balance: $1300.00
```

### 4. Transfer Money
Send money to another account:
- Enter the destination account number
- Enter the amount to transfer
- Confirm with your password
- Both accounts are updated immediately

**Example:**
```
Enter your choice: 4

--- Transfer Money ---
Enter destination account number: ACC002
Enter amount to transfer: $300
Enter your password to confirm: ****

✅ Transfer successful
💰 New Balance: $1000.00
```

### 5. View Transaction History
See all your account transactions:
- Date and time of transaction
- Description
- Transaction type (CREDIT or DEBIT)
- Amount
- Balance after transaction

**Example:**
```
Enter your choice: 5

--- Transaction History ---

Date                 Description               Type       Amount       Balance
------------------------------------------------------------------------------------------
2024-01-27 10:00:00  Initial Deposit           CREDIT     $1000.00     $1000.00
2024-01-27 10:05:00  Salary payment            CREDIT     $500.00      $1500.00
2024-01-27 10:10:00  ATM withdrawal            DEBIT      $200.00      $1300.00
2024-01-27 10:15:00  Transfer to ACC002        DEBIT      $300.00      $1000.00
```

### 6. Logout
Return to the main menu and logout from your account.

### 7. Exit
Close the application.

## Features

### Security Features
- **Password Hashing**: All passwords are hashed using SHA-256
- **Authentication Required**: Must login to access account operations
- **Transfer Verification**: Password required for money transfers
- **Input Validation**: All inputs are validated before processing

### Data Management
- **Automatic Saving**: All changes are automatically saved
- **Data Persistence**: Your data is stored in `bank_data.json`
- **Transaction History**: Complete record of all account activities

## Troubleshooting

### Cannot Create Account
- **Account already exists**: Choose a different account number
- **Passwords don't match**: Make sure to enter the same password twice
- **Negative balance**: Initial balance must be 0 or greater

### Cannot Login
- **Invalid credentials**: Check your account number and password
- **Account doesn't exist**: Create an account first

### Cannot Withdraw
- **Insufficient balance**: Check your balance first
- **Invalid amount**: Amount must be greater than 0

### Cannot Transfer
- **Destination not found**: Verify the destination account number
- **Insufficient balance**: Check your balance first
- **Wrong password**: Enter the correct password to confirm

## Tips
- Keep your account number and password secure
- Use descriptive transaction descriptions for better tracking
- Regularly check your transaction history
- Always verify the destination account before transferring

## Support
For issues and questions, please open an issue on the GitHub repository:
https://github.com/JuliusYawli/ortfolio-Task-1-Banking-Tools/issues
