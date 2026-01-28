# Banking Tools

A comprehensive command-line banking application with account management, transactions, and security features.

## Features

- 🏦 **Account Management**: Create and manage bank accounts
- 💰 **Deposits & Withdrawals**: Easy money management
- 💸 **Transfers**: Transfer money between accounts
- 📊 **Transaction History**: View detailed transaction records
- 🔒 **Security**: Password-protected accounts with SHA-256 hashing
- 💾 **Data Persistence**: All data saved in JSON format

## Installation

1. Clone the repository:
```bash
git clone https://github.com/JuliusYawli/ortfolio-Task-1-Banking-Tools.git
cd ortfolio-Task-1-Banking-Tools
```

2. No external dependencies required! Uses Python standard library only.

## Requirements

- Python 3.7 or higher

## Usage

Run the application:
```bash
python3 main.py
```

### Main Features

#### 1. Create New Account
- Enter a unique account number
- Provide account holder name
- Set a secure password
- Optionally add an initial balance

#### 2. Login
- Use your account number and password
- Access all banking features

#### 3. Banking Operations (After Login)
- **Check Balance**: View your current balance
- **Deposit Money**: Add funds to your account
- **Withdraw Money**: Take money from your account
- **Transfer Money**: Send money to another account
- **View Transaction History**: See all your transactions

## Example Usage

```
===================================================
               BANKING TOOLS
===================================================

1. Create New Account
2. Login to Existing Account
3. View All Accounts (Admin)
4. Exit
===================================================

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

## Data Storage

Account data is stored in `bank_data.json` in the project root directory. This file is automatically created when you first create an account.

## Security Features

- **Password Hashing**: All passwords are hashed using SHA-256
  - Note: For production use, consider using more secure algorithms like bcrypt or PBKDF2
- **Authentication Required**: Must login to access account operations
- **Transfer Verification**: Password required for money transfers
- **Input Validation**: All inputs are validated before processing
- **Password Requirements**: Minimum 4 characters
- **Self-Transfer Prevention**: Cannot transfer money to the same account

## Project Structure

```
ortfolio-Task-1-Banking-Tools/
├── banking_tools/
│   ├── __init__.py      # Package initialization
│   ├── account.py       # Account class with operations
│   └── bank.py          # Bank management system
├── tests/               # Test files
├── main.py             # Main CLI application
├── README.md           # This file
└── requirements.txt    # Python dependencies (none required)
```

## Testing

Run the test suite:
```bash
python3 tests/test_account.py
python3 tests/test_bank.py
```

Or use the setup script which runs all tests:
```bash
./setup.sh
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is open source and available under the MIT License.

## Author

Banking Tools Team

## Support

For issues and questions, please open an issue on the GitHub repository.