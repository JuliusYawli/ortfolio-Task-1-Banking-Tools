"""Tests for the Account class."""

import sys
import os
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from banking_tools.account import Account


def test_account_creation():
    """Test creating a new account."""
    account = Account("ACC001", "John Doe", "password123", 1000.0)
    assert account.account_number == "ACC001"
    assert account.account_holder == "John Doe"
    assert account.balance == 1000.0


def test_password_verification():
    """Test password verification."""
    account = Account("ACC001", "John Doe", "password123", 1000.0)
    assert account.verify_password("password123") is True
    assert account.verify_password("wrongpassword") is False


def test_deposit():
    """Test depositing money."""
    account = Account("ACC001", "John Doe", "password123", 1000.0)
    assert account.deposit(500.0) is True
    assert account.balance == 1500.0


def test_deposit_negative():
    """Test depositing negative amount."""
    account = Account("ACC001", "John Doe", "password123", 1000.0)
    assert account.deposit(-500.0) is False
    assert account.balance == 1000.0


def test_withdraw():
    """Test withdrawing money."""
    account = Account("ACC001", "John Doe", "password123", 1000.0)
    assert account.withdraw(300.0) is True
    assert account.balance == 700.0


def test_withdraw_insufficient_balance():
    """Test withdrawing more than balance."""
    account = Account("ACC001", "John Doe", "password123", 1000.0)
    assert account.withdraw(1500.0) is False
    assert account.balance == 1000.0


def test_withdraw_negative():
    """Test withdrawing negative amount."""
    account = Account("ACC001", "John Doe", "password123", 1000.0)
    assert account.withdraw(-100.0) is False
    assert account.balance == 1000.0


def test_get_balance():
    """Test getting account balance."""
    account = Account("ACC001", "John Doe", "password123", 1000.0)
    assert account.get_balance() == 1000.0


def test_transaction_history():
    """Test transaction history tracking."""
    account = Account("ACC001", "John Doe", "password123", 1000.0)
    account.deposit(500.0, "Test deposit")
    account.withdraw(200.0, "Test withdrawal")
    
    history = account.get_transaction_history()
    assert len(history) == 3  # Initial deposit + 2 transactions
    assert history[-1]["description"] == "Test withdrawal"
    assert history[-1]["amount"] == 200.0


def test_to_dict_and_from_dict():
    """Test serialization and deserialization."""
    account = Account("ACC001", "John Doe", "password123", 1000.0)
    account.deposit(500.0)
    
    data = account.to_dict()
    restored_account = Account.from_dict(data)
    
    assert restored_account.account_number == account.account_number
    assert restored_account.account_holder == account.account_holder
    assert restored_account.balance == account.balance
    assert len(restored_account.transactions) == len(account.transactions)


if __name__ == "__main__":
    test_account_creation()
    test_password_verification()
    test_deposit()
    test_deposit_negative()
    test_withdraw()
    test_withdraw_insufficient_balance()
    test_withdraw_negative()
    test_get_balance()
    test_transaction_history()
    test_to_dict_and_from_dict()
    print("✅ All account tests passed!")
