# Banking Tools

A comprehensive command-line banking application written in Java with account management, transactions, and security features.

## Features

- 🏦 **Account Management**: Create and manage bank accounts
- 💰 **Deposits & Withdrawals**: Easy money management
- 💸 **Transfers**: Transfer money between accounts
- 📊 **Transaction History**: View detailed transaction records
- 🔒 **Security**: Password-protected accounts with SHA-256 hashing
- 💾 **Data Persistence**: All data saved in JSON format

## Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build Instructions

1. Clone the repository:
```bash
git clone https://github.com/JuliusYawli/ortfolio-Task-1-Banking-Tools.git
cd ortfolio-Task-1-Banking-Tools
```

2. Build the project:
```bash
mvn clean package
```

This will create an executable JAR file: `target/banking-tools.jar`

## Usage

### Running the Application

```bash
java -jar target/banking-tools.jar
```

Or using Maven:
```bash
mvn exec:java -Dexec.mainClass="com.banking.BankingCLI"
```

### Running the Demo

```bash
mvn exec:java -Dexec.mainClass="com.banking.Demo"
```

### Main Features

#### 1. Create New Account
- Enter a unique account number
- Provide account holder name
- Set a secure password (minimum 4 characters)
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
==================================================
               BANKING TOOLS
==================================================

1. Create New Account
2. Login to Existing Account
3. View All Accounts (Admin)
4. Exit
==================================================

Enter your choice: 1

--- Create New Account ---
Enter account number: ACC001
Enter account holder name: John Doe
Enter password (min 4 characters): ****
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
  - Note: For production use, consider using bcrypt or PBKDF2
- **Authentication Required**: Must login to access account operations
- **Transfer Verification**: Password required for money transfers
- **Input Validation**: All inputs are validated before processing
- **Password Requirements**: Minimum 4 characters
- **Self-Transfer Prevention**: Cannot transfer money to the same account
- **Admin Access**: Viewing all accounts requires admin password (default: admin123)

## Important Notes

This is an educational project demonstrating banking concepts. For production use, consider:

- Using `BigDecimal` instead of `double` for financial calculations
- Implementing proper multi-threading support with synchronized operations
- Using bcrypt or PBKDF2 for password hashing instead of SHA-256
- Implementing proper admin authentication with role-based access control
- Using a production database instead of JSON files
- Adding audit logging for all transactions
- Implementing session management and timeouts
- Adding transaction rollback capabilities

## Project Structure

```
ortfolio-Task-1-Banking-Tools/
├── src/
│   ├── main/java/com/banking/
│   │   ├── Account.java          # Account class with operations
│   │   ├── Bank.java             # Bank management system
│   │   ├── BankingCLI.java       # CLI application
│   │   └── Demo.java             # Demo script
│   └── test/java/com/banking/
│       ├── AccountTest.java      # Account tests (13 tests)
│       └── BankTest.java         # Bank tests (15 tests)
├── pom.xml                       # Maven configuration
└── README.md                     # This file
```

## Testing

Run the test suite:
```bash
mvn test
```

Test results:
- Account tests: 13 test cases
- Bank tests: 15 test cases
- Total: 28 tests, all passing

## Building

Create executable JAR:
```bash
mvn package
```

The packaged JAR includes all dependencies and can be run with:
```bash
java -jar target/banking-tools.jar
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is open source and available under the MIT License.

## Author

Banking Tools Team

## Support

For issues and questions, please open an issue on the GitHub repository.
