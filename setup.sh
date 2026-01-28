#!/bin/bash

# Banking Tools Setup Script

echo "🏦 Banking Tools Setup"
echo "======================"
echo ""

# Check Python version
python_version=$(python3 --version 2>&1 | awk '{print $2}')
echo "✓ Python version: $python_version"

# Check if Python 3.7+
required_version="3.7"
if [ "$(printf '%s\n' "$required_version" "$python_version" | sort -V | head -n1)" = "$required_version" ]; then 
    echo "✓ Python version meets requirements (3.7+)"
else
    echo "✗ Python 3.7 or higher is required"
    exit 1
fi

# Run tests
echo ""
echo "Running tests..."
python3 tests/test_account.py
python3 tests/test_bank.py

echo ""
echo "✅ Setup complete! You can now run the application:"
echo "   python3 main.py"
