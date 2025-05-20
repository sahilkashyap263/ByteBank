package com.bytebank.service;

import com.bytebank.dao.AccountDAO;
import com.bytebank.model.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public class AccountService {
    private AccountDAO accountDAO;
    
    public AccountService() {
        this.accountDAO = new AccountDAO();
    }
    
    public Account createAccount(int userId, Account.AccountType accountType) {
        // Generate a unique account number
        String accountNumber = generateAccountNumber();
        
        // Create the account
        Account account = new Account(userId, accountNumber, accountType);
        
        if (accountDAO.addAccount(account)) {
            return account;
        }
        
        return null;
    }
    
    private String generateAccountNumber() {
        // Generate a random 10-digit account number
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < 10; i++) {
            sb.append(rand.nextInt(10));
        }
        
        return sb.toString();
    }
    
    public Account getAccountById(int accountId) {
        return accountDAO.getAccountById(accountId);
    }
    
    public Account getAccountByAccountNumber(String accountNumber) {
        return accountDAO.getAccountByAccountNumber(accountNumber);
    }
    
    public List<Account> getAccountsByUserId(int userId) {
        return accountDAO.getAccountsByUserId(userId);
    }
    
    public boolean deposit(int accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        Account account = accountDAO.getAccountById(accountId);
        
        if (account == null) {
            return false;
        }
        
        BigDecimal newBalance = account.getBalance().add(amount);
        return accountDAO.updateAccountBalance(accountId, newBalance);
    }
    
    public boolean withdraw(int accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        Account account = accountDAO.getAccountById(accountId);
        
        if (account == null) {
            return false;
        }
        
        // Check if sufficient balance
        if (account.getBalance().compareTo(amount) < 0) {
            return false;
        }
        
        BigDecimal newBalance = account.getBalance().subtract(amount);
        return accountDAO.updateAccountBalance(accountId, newBalance);
    }
}