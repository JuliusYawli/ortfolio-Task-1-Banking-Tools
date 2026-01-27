"""Account management module for banking operations."""

import json
import hashlib
from datetime import datetime
from typing import List, Dict, Optional


class Account:
    """Represents a bank account with basic operations."""
    
    def __init__(self, account_number: str, account_holder: str, 
                 password: str, initial_balance: float = 0.0):
        """
        Initialize a new bank account.
        
        Args:
            account_number: Unique account identifier
            account_holder: Name of the account holder
            password: Account password (will be hashed)
            initial_balance: Starting balance (default: 0.0)
        """
        self.account_number = account_number
        self.account_holder = account_holder
        self.password_hash = self._hash_password(password)
        self.balance = initial_balance
        self.transactions: List[Dict] = []
        self.created_at = datetime.now().isoformat()
        
        if initial_balance > 0:
            self._add_transaction("Initial Deposit", initial_balance, "credit")
    
    @staticmethod
    def _hash_password(password: str) -> str:
        """Hash password using SHA-256."""
        return hashlib.sha256(password.encode()).hexdigest()
    
    def verify_password(self, password: str) -> bool:
        """Verify if the provided password is correct."""
        return self.password_hash == self._hash_password(password)
    
    def _add_transaction(self, description: str, amount: float, 
                        transaction_type: str) -> None:
        """Add a transaction to the account history."""
        transaction = {
            "date": datetime.now().isoformat(),
            "description": description,
            "amount": amount,
            "type": transaction_type,
            "balance_after": self.balance
        }
        self.transactions.append(transaction)
    
    def deposit(self, amount: float, description: str = "Deposit") -> bool:
        """
        Deposit money into the account.
        
        Args:
            amount: Amount to deposit
            description: Transaction description
            
        Returns:
            True if successful, False otherwise
        """
        if amount <= 0:
            return False
        
        self.balance += amount
        self._add_transaction(description, amount, "credit")
        return True
    
    def withdraw(self, amount: float, description: str = "Withdrawal") -> bool:
        """
        Withdraw money from the account.
        
        Args:
            amount: Amount to withdraw
            description: Transaction description
            
        Returns:
            True if successful, False otherwise
        """
        if amount <= 0 or amount > self.balance:
            return False
        
        self.balance -= amount
        self._add_transaction(description, amount, "debit")
        return True
    
    def get_balance(self) -> float:
        """Get current account balance."""
        return self.balance
    
    def get_transaction_history(self, limit: Optional[int] = None) -> List[Dict]:
        """
        Get transaction history.
        
        Args:
            limit: Maximum number of transactions to return (None for all)
            
        Returns:
            List of transaction dictionaries
        """
        if limit:
            return self.transactions[-limit:]
        return self.transactions
    
    def to_dict(self) -> Dict:
        """Convert account to dictionary for serialization."""
        return {
            "account_number": self.account_number,
            "account_holder": self.account_holder,
            "password_hash": self.password_hash,
            "balance": self.balance,
            "transactions": self.transactions,
            "created_at": self.created_at
        }
    
    @classmethod
    def from_dict(cls, data: Dict) -> 'Account':
        """Create account from dictionary."""
        account = cls.__new__(cls)
        account.account_number = data["account_number"]
        account.account_holder = data["account_holder"]
        account.password_hash = data["password_hash"]
        account.balance = data["balance"]
        account.transactions = data["transactions"]
        account.created_at = data["created_at"]
        return account
