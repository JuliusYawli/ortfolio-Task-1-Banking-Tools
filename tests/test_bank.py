"""Tests for the Bank class."""

import sys
import os
import tempfile
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from banking_tools.bank import Bank


def test_create_account():
    """Test creating a new account in the bank."""
    with tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.json') as f:
        temp_file = f.name
    
    try:
        bank = Bank(temp_file)
        success = bank.create_account("ACC001", "John Doe", "password123", 1000.0)
        assert success is True
        
        account = bank.get_account("ACC001")
        assert account is not None
        assert account.account_holder == "John Doe"
    finally:
        if os.path.exists(temp_file):
            os.unlink(temp_file)


def test_create_duplicate_account():
    """Test creating duplicate account."""
    with tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.json') as f:
        temp_file = f.name
    
    try:
        bank = Bank(temp_file)
        bank.create_account("ACC001", "John Doe", "password123", 1000.0)
        success = bank.create_account("ACC001", "Jane Doe", "password456", 500.0)
        assert success is False
    finally:
        if os.path.exists(temp_file):
            os.unlink(temp_file)


def test_authenticate():
    """Test account authentication."""
    with tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.json') as f:
        temp_file = f.name
    
    try:
        bank = Bank(temp_file)
        bank.create_account("ACC001", "John Doe", "password123", 1000.0)
        
        # Test correct credentials
        account = bank.authenticate("ACC001", "password123")
        assert account is not None
        
        # Test wrong password
        account = bank.authenticate("ACC001", "wrongpassword")
        assert account is None
        
        # Test non-existent account
        account = bank.authenticate("ACC999", "password123")
        assert account is None
    finally:
        if os.path.exists(temp_file):
            os.unlink(temp_file)


def test_transfer():
    """Test money transfer between accounts."""
    with tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.json') as f:
        temp_file = f.name
    
    try:
        bank = Bank(temp_file)
        bank.create_account("ACC001", "John Doe", "password123", 1000.0)
        bank.create_account("ACC002", "Jane Doe", "password456", 500.0)
        
        # Test successful transfer
        success, message = bank.transfer("ACC001", "ACC002", 300.0, "password123")
        assert success is True
        assert message == "Transfer successful"
        
        acc1 = bank.get_account("ACC001")
        acc2 = bank.get_account("ACC002")
        assert acc1.balance == 700.0
        assert acc2.balance == 800.0
    finally:
        if os.path.exists(temp_file):
            os.unlink(temp_file)


def test_transfer_insufficient_balance():
    """Test transfer with insufficient balance."""
    with tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.json') as f:
        temp_file = f.name
    
    try:
        bank = Bank(temp_file)
        bank.create_account("ACC001", "John Doe", "password123", 500.0)
        bank.create_account("ACC002", "Jane Doe", "password456", 500.0)
        
        success, message = bank.transfer("ACC001", "ACC002", 1000.0, "password123")
        assert success is False
        assert "Insufficient balance" in message
    finally:
        if os.path.exists(temp_file):
            os.unlink(temp_file)


def test_transfer_wrong_password():
    """Test transfer with wrong password."""
    with tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.json') as f:
        temp_file = f.name
    
    try:
        bank = Bank(temp_file)
        bank.create_account("ACC001", "John Doe", "password123", 1000.0)
        bank.create_account("ACC002", "Jane Doe", "password456", 500.0)
        
        success, message = bank.transfer("ACC001", "ACC002", 300.0, "wrongpassword")
        assert success is False
        assert "Authentication failed" in message
    finally:
        if os.path.exists(temp_file):
            os.unlink(temp_file)


def test_data_persistence():
    """Test that data is saved and loaded correctly."""
    with tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.json') as f:
        temp_file = f.name
    
    try:
        # Create bank and add accounts
        bank1 = Bank(temp_file)
        bank1.create_account("ACC001", "John Doe", "password123", 1000.0)
        bank1.create_account("ACC002", "Jane Doe", "password456", 500.0)
        
        # Create new bank instance (should load data)
        bank2 = Bank(temp_file)
        account = bank2.get_account("ACC001")
        
        assert account is not None
        assert account.account_holder == "John Doe"
        assert account.balance == 1000.0
    finally:
        if os.path.exists(temp_file):
            os.unlink(temp_file)


def test_list_accounts():
    """Test listing all accounts."""
    with tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.json') as f:
        temp_file = f.name
    
    try:
        bank = Bank(temp_file)
        bank.create_account("ACC001", "John Doe", "password123", 1000.0)
        bank.create_account("ACC002", "Jane Doe", "password456", 500.0)
        
        accounts = bank.list_accounts()
        assert len(accounts) == 2
        assert any(acc['account_number'] == 'ACC001' for acc in accounts)
        assert any(acc['account_number'] == 'ACC002' for acc in accounts)
    finally:
        if os.path.exists(temp_file):
            os.unlink(temp_file)


if __name__ == "__main__":
    test_create_account()
    test_create_duplicate_account()
    test_authenticate()
    test_transfer()
    test_transfer_insufficient_balance()
    test_transfer_wrong_password()
    test_data_persistence()
    test_list_accounts()
    print("✅ All bank tests passed!")
