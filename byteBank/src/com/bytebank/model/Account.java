package com.bytebank.model;

import java.math.BigDecimal;
import java.util.Date;

public class Account {
    private int accountId;
    private int userId;
    private String accountNumber;
    private BigDecimal balance;
    private AccountType accountType;
    private Date creationDate;
    
    public enum AccountType {
        SAVINGS, CHECKING
    }
    
    // Constructors
    public Account() {
        this.balance = BigDecimal.ZERO;
    }
    
    public Account(int userId, String accountNumber, AccountType accountType) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = BigDecimal.ZERO;
    }
    
    // Getters and Setters
    public int getAccountId() {
        return accountId;
    }
    
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public AccountType getAccountType() {
        return accountType;
    }
    
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    
    public Date getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}