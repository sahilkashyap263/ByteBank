package com.bytebank.model;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private int transactionId;
    private int accountId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private String description;
    private Integer recipientAccountId;
    private Date transactionDate;
    
    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT
    }
    
    // Constructors
    public Transaction() {}
    
    public Transaction(int accountId, TransactionType transactionType, 
                      BigDecimal amount, String description) {
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
    }
    
    public Transaction(int accountId, TransactionType transactionType, 
                      BigDecimal amount, String description, Integer recipientAccountId) {
        this(accountId, transactionType, amount, description);
        this.recipientAccountId = recipientAccountId;
    }
    
    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
    
    public int getAccountId() {
        return accountId;
    }
    
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    
    public TransactionType getTransactionType() {
        return transactionType;
    }
    
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getRecipientAccountId() {
        return recipientAccountId;
    }
    
    public void setRecipientAccountId(Integer recipientAccountId) {
        this.recipientAccountId = recipientAccountId;
    }
    
    public Date getTransactionDate() {
        return transactionDate;
    }
    
    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}