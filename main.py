#!/usr/bin/env python3
"""Main CLI application for Banking Tools."""

import sys
import getpass
from banking_tools.bank import Bank


class BankingCLI:
    """Command-line interface for banking operations."""
    
    def __init__(self):
        """Initialize the banking CLI."""
        self.bank = Bank()
        self.current_account = None
    
    def clear_screen(self):
        """Clear the screen (cross-platform)."""
        import os
        os.system('cls' if os.name == 'nt' else 'clear')
    
    def display_menu(self):
        """Display main menu."""
        print("\n" + "="*50)
        print(" "*15 + "BANKING TOOLS")
        print("="*50)
        
        if self.current_account:
            print(f"\nLogged in as: {self.current_account.account_holder}")
            print(f"Account: {self.current_account.account_number}")
            print(f"Balance: ${self.current_account.get_balance():.2f}")
            print("\n1. Check Balance")
            print("2. Deposit Money")
            print("3. Withdraw Money")
            print("4. Transfer Money")
            print("5. View Transaction History")
            print("6. Logout")
            print("7. Exit")
        else:
            print("\n1. Create New Account")
            print("2. Login to Existing Account")
            print("3. View All Accounts (Admin)")
            print("4. Exit")
        
        print("="*50)
    
    def create_account(self):
        """Create a new bank account."""
        print("\n--- Create New Account ---")
        account_number = input("Enter account number: ").strip()
        
        if not account_number:
            print("❌ Account number cannot be empty!")
            return
        
        if self.bank.get_account(account_number):
            print("❌ Account already exists!")
            return
        
        account_holder = input("Enter account holder name: ").strip()
        if not account_holder:
            print("❌ Name cannot be empty!")
            return
        
        password = getpass.getpass("Enter password: ")
        password_confirm = getpass.getpass("Confirm password: ")
        
        if password != password_confirm:
            print("❌ Passwords do not match!")
            return
        
        try:
            initial_balance = float(input("Enter initial balance (0 or more): $"))
            if initial_balance < 0:
                print("❌ Initial balance cannot be negative!")
                return
        except ValueError:
            print("❌ Invalid amount!")
            return
        
        if self.bank.create_account(account_number, account_holder, password, initial_balance):
            print(f"\n✅ Account created successfully!")
            print(f"Account Number: {account_number}")
            print(f"Account Holder: {account_holder}")
            print(f"Initial Balance: ${initial_balance:.2f}")
        else:
            print("❌ Failed to create account!")
    
    def login(self):
        """Login to an existing account."""
        print("\n--- Login ---")
        account_number = input("Enter account number: ").strip()
        password = getpass.getpass("Enter password: ")
        
        account = self.bank.authenticate(account_number, password)
        if account:
            self.current_account = account
            print(f"\n✅ Welcome, {account.account_holder}!")
        else:
            print("❌ Invalid account number or password!")
    
    def check_balance(self):
        """Display current balance."""
        if not self.current_account:
            print("❌ Please login first!")
            return
        
        print(f"\n💰 Current Balance: ${self.current_account.get_balance():.2f}")
    
    def deposit(self):
        """Deposit money into account."""
        if not self.current_account:
            print("❌ Please login first!")
            return
        
        print("\n--- Deposit Money ---")
        try:
            amount = float(input("Enter amount to deposit: $"))
            description = input("Description (optional): ").strip() or "Deposit"
            
            if self.current_account.deposit(amount, description):
                self.bank.save_data()
                print(f"\n✅ Deposited ${amount:.2f} successfully!")
                print(f"💰 New Balance: ${self.current_account.get_balance():.2f}")
            else:
                print("❌ Invalid deposit amount!")
        except ValueError:
            print("❌ Invalid amount!")
    
    def withdraw(self):
        """Withdraw money from account."""
        if not self.current_account:
            print("❌ Please login first!")
            return
        
        print("\n--- Withdraw Money ---")
        try:
            amount = float(input("Enter amount to withdraw: $"))
            description = input("Description (optional): ").strip() or "Withdrawal"
            
            if self.current_account.withdraw(amount, description):
                self.bank.save_data()
                print(f"\n✅ Withdrew ${amount:.2f} successfully!")
                print(f"💰 New Balance: ${self.current_account.get_balance():.2f}")
            else:
                print("❌ Invalid amount or insufficient balance!")
        except ValueError:
            print("❌ Invalid amount!")
    
    def transfer(self):
        """Transfer money to another account."""
        if not self.current_account:
            print("❌ Please login first!")
            return
        
        print("\n--- Transfer Money ---")
        to_account = input("Enter destination account number: ").strip()
        
        try:
            amount = float(input("Enter amount to transfer: $"))
            password = getpass.getpass("Enter your password to confirm: ")
            
            success, message = self.bank.transfer(
                self.current_account.account_number,
                to_account,
                amount,
                password
            )
            
            if success:
                print(f"\n✅ {message}")
                print(f"💰 New Balance: ${self.current_account.get_balance():.2f}")
            else:
                print(f"❌ {message}")
        except ValueError:
            print("❌ Invalid amount!")
    
    def view_transactions(self):
        """View transaction history."""
        if not self.current_account:
            print("❌ Please login first!")
            return
        
        print("\n--- Transaction History ---")
        transactions = self.current_account.get_transaction_history()
        
        if not transactions:
            print("No transactions found.")
            return
        
        print(f"\n{'Date':<20} {'Description':<25} {'Type':<10} {'Amount':<12} {'Balance'}")
        print("-" * 90)
        
        for trans in transactions:
            date = trans['date'][:19]  # Trim milliseconds
            desc = trans['description'][:24]
            trans_type = trans['type'].upper()
            amount = f"${trans['amount']:.2f}"
            balance = f"${trans['balance_after']:.2f}"
            
            print(f"{date:<20} {desc:<25} {trans_type:<10} {amount:<12} {balance}")
    
    def view_all_accounts(self):
        """View all accounts (admin feature)."""
        print("\n--- All Accounts ---")
        accounts = self.bank.list_accounts()
        
        if not accounts:
            print("No accounts found.")
            return
        
        print(f"\n{'Account Number':<20} {'Account Holder':<30} {'Balance'}")
        print("-" * 70)
        
        for acc in accounts:
            print(f"{acc['account_number']:<20} {acc['account_holder']:<30} ${acc['balance']:.2f}")
    
    def logout(self):
        """Logout from current account."""
        if self.current_account:
            print(f"\n👋 Goodbye, {self.current_account.account_holder}!")
            self.current_account = None
        else:
            print("❌ Not logged in!")
    
    def run(self):
        """Run the main application loop."""
        print("\n✨ Welcome to Banking Tools! ✨")
        
        while True:
            self.display_menu()
            choice = input("\nEnter your choice: ").strip()
            
            if self.current_account:
                # Logged in menu
                if choice == "1":
                    self.check_balance()
                elif choice == "2":
                    self.deposit()
                elif choice == "3":
                    self.withdraw()
                elif choice == "4":
                    self.transfer()
                elif choice == "5":
                    self.view_transactions()
                elif choice == "6":
                    self.logout()
                elif choice == "7":
                    print("\n👋 Thank you for using Banking Tools!")
                    sys.exit(0)
                else:
                    print("❌ Invalid choice! Please try again.")
            else:
                # Not logged in menu
                if choice == "1":
                    self.create_account()
                elif choice == "2":
                    self.login()
                elif choice == "3":
                    self.view_all_accounts()
                elif choice == "4":
                    print("\n👋 Thank you for using Banking Tools!")
                    sys.exit(0)
                else:
                    print("❌ Invalid choice! Please try again.")
            
            input("\nPress Enter to continue...")


def main():
    """Main entry point."""
    try:
        cli = BankingCLI()
        cli.run()
    except KeyboardInterrupt:
        print("\n\n👋 Thank you for using Banking Tools!")
        sys.exit(0)
    except Exception as e:
        print(f"\n❌ An error occurred: {e}")
        sys.exit(1)


if __name__ == "__main__":
    main()
