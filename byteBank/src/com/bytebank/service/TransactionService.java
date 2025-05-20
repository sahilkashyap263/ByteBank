package com.bytebank.service;

import com.bytebank.dao.TransactionDAO;
import com.bytebank.model.Account;
import com.bytebank.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class TransactionService {
    private TransactionDAO transactionDAO;
    private AccountService accountService;
    
    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
        this.accountService = new AccountService();
    }
    
    public boolean recordDeposit(int accountId, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        // Create transaction record
        Transaction transaction = new Transaction(
            accountId, 
            Transaction.TransactionType.DEPOSIT,
            amount,
            description
        );
        
        // Update account balance
        if (accountService.deposit(accountId, amount)) {
            return transactionDAO.addTransaction(transaction);
        }
        return false;
    }
    
    public boolean recordWithdrawal(int accountId, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        // Create transaction record
        Transaction transaction = new Transaction(
            accountId, 
            Transaction.TransactionType.WITHDRAWAL,
            amount,
            description
        );
        
        // Update account balance
        if (accountService.withdraw(accountId, amount)) {
            return transactionDAO.addTransaction(transaction);
        }
        
        return false;
    }
    
    public boolean transferFunds(int fromAccountId, int toAccountId, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        // Check if accounts exist
        Account fromAccount = accountService.getAccountById(fromAccountId);
        Account toAccount = accountService.getAccountById(toAccountId);
        
        if (fromAccount == null || toAccount == null) {
            return false;
        }
        
        // Check if sufficient balance
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            return false;
        }
        
        // Create outgoing transaction
        Transaction outgoingTransaction = new Transaction(
            fromAccountId, 
            Transaction.TransactionType.TRANSFER_OUT,
            amount,
            description,
            toAccountId
        );
        
        // Create incoming transaction
        Transaction incomingTransaction = new Transaction(
            toAccountId, 
            Transaction.TransactionType.TRANSFER_IN,
            amount,
            description,
            fromAccountId
        );
        
        // Withdraw from source account
        if (!accountService.withdraw(fromAccountId, amount)) {
            return false;
        }
        
        // Deposit to destination account
        if (!accountService.deposit(toAccountId, amount)) {
            // Rollback withdrawal
            accountService.deposit(fromAccountId, amount);
            return false;
        }
        
        // Record transactions
        boolean outSuccess = transactionDAO.addTransaction(outgoingTransaction);
        boolean inSuccess = transactionDAO.addTransaction(incomingTransaction);
        
        if (!outSuccess || !inSuccess) {
            // Transaction recording failed, but money has been transferred
            // In a real system, this would need proper error handling and logging
            return false;
        }
        
        return true;
    }
    
    public List<Transaction> getTransactionHistory(int accountId) {
        return transactionDAO.getTransactionsByAccountId(accountId);
    }
}