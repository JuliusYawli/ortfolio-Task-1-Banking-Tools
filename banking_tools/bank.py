"""Bank management system."""

import json
import os
from typing import Optional, List, Dict
from .account import Account


class Bank:
    """Main bank class managing multiple accounts."""
    
    def __init__(self, data_file: str = "bank_data.json"):
        """
        Initialize the bank.
        
        Args:
            data_file: Path to the data persistence file
        """
        self.data_file = data_file
        self.accounts: Dict[str, Account] = {}
        self.load_data()
    
    def load_data(self) -> None:
        """Load account data from file."""
        if os.path.exists(self.data_file):
            try:
                with open(self.data_file, 'r') as f:
                    data = json.load(f)
                    for acc_data in data:
                        account = Account.from_dict(acc_data)
                        self.accounts[account.account_number] = account
            except (json.JSONDecodeError, KeyError):
                # If file is corrupted, start fresh
                self.accounts = {}
    
    def save_data(self) -> None:
        """Save account data to file."""
        data = [account.to_dict() for account in self.accounts.values()]
        with open(self.data_file, 'w') as f:
            json.dump(data, f, indent=2)
    
    def create_account(self, account_number: str, account_holder: str,
                      password: str, initial_balance: float = 0.0) -> bool:
        """
        Create a new bank account.
        
        Args:
            account_number: Unique account identifier
            account_holder: Name of the account holder
            password: Account password
            initial_balance: Starting balance
            
        Returns:
            True if account created successfully, False if account already exists
        """
        if account_number in self.accounts:
            return False
        
        if initial_balance < 0:
            return False
        
        account = Account(account_number, account_holder, password, initial_balance)
        self.accounts[account_number] = account
        self.save_data()
        return True
    
    def authenticate(self, account_number: str, password: str) -> Optional[Account]:
        """
        Authenticate and return account.
        
        Args:
            account_number: Account identifier
            password: Account password
            
        Returns:
            Account object if authentication successful, None otherwise
        """
        account = self.accounts.get(account_number)
        if account and account.verify_password(password):
            return account
        return None
    
    def get_account(self, account_number: str) -> Optional[Account]:
        """Get account by account number (without authentication)."""
        return self.accounts.get(account_number)
    
    def transfer(self, from_account: str, to_account: str, amount: float,
                password: str) -> tuple[bool, str]:
        """
        Transfer money between accounts.
        
        Args:
            from_account: Source account number
            to_account: Destination account number
            amount: Amount to transfer
            password: Source account password
            
        Returns:
            Tuple of (success: bool, message: str)
        """
        # Authenticate source account
        source = self.authenticate(from_account, password)
        if not source:
            return False, "Authentication failed"
        
        # Check if destination account exists
        destination = self.get_account(to_account)
        if not destination:
            return False, "Destination account not found"
        
        # Check if source has sufficient balance
        if amount <= 0:
            return False, "Invalid amount"
        
        if source.balance < amount:
            return False, "Insufficient balance"
        
        # Perform transfer
        source.withdraw(amount, f"Transfer to {to_account}")
        destination.deposit(amount, f"Transfer from {from_account}")
        self.save_data()
        
        return True, "Transfer successful"
    
    def list_accounts(self) -> List[Dict]:
        """Get list of all accounts (basic info only)."""
        return [
            {
                "account_number": acc.account_number,
                "account_holder": acc.account_holder,
                "balance": acc.balance
            }
            for acc in self.accounts.values()
        ]
