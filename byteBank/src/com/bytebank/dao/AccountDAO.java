package com.bytebank.dao;

import com.bytebank.model.Account;
import com.bytebank.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    
    public boolean addAccount(Account account) {
        String query = "INSERT INTO accounts (user_id, account_number, account_type) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, account.getUserId());
            pstmt.setString(2, account.getAccountNumber());
            pstmt.setString(3, account.getAccountType().toString());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.setAccountId(generatedKeys.getInt(1));
                } else {
                    return false;
                }
            }
            
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public Account getAccountById(int accountId) {
        String query = "SELECT * FROM accounts WHERE account_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Account account = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, accountId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                account = new Account();
                account.setAccountId(rs.getInt("account_id"));
                account.setUserId(rs.getInt("user_id"));
                account.setAccountNumber(rs.getString("account_number"));
                account.setBalance(rs.getBigDecimal("balance"));
                account.setAccountType(Account.AccountType.valueOf(rs.getString("account_type")));
                account.setCreationDate(rs.getTimestamp("creation_date"));
            }
            
            return account;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public Account getAccountByAccountNumber(String accountNumber) {
        String query = "SELECT * FROM accounts WHERE account_number = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Account account = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, accountNumber);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                account = new Account();
                account.setAccountId(rs.getInt("account_id"));
                account.setUserId(rs.getInt("user_id"));
                account.setAccountNumber(rs.getString("account_number"));
                account.setBalance(rs.getBigDecimal("balance"));
                account.setAccountType(Account.AccountType.valueOf(rs.getString("account_type")));
                account.setCreationDate(rs.getTimestamp("creation_date"));
            }
            
            return account;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public List<Account> getAccountsByUserId(int userId) {
        String query = "SELECT * FROM accounts WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Account> accounts = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Account account = new Account();
                account.setAccountId(rs.getInt("account_id"));
                account.setUserId(rs.getInt("user_id"));
                account.setAccountNumber(rs.getString("account_number"));
                account.setBalance(rs.getBigDecimal("balance"));
                account.setAccountType(Account.AccountType.valueOf(rs.getString("account_type")));
                account.setCreationDate(rs.getTimestamp("creation_date"));
                accounts.add(account);
            }
            
            return accounts;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean updateAccountBalance(int accountId, java.math.BigDecimal newBalance) {
        String query = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setBigDecimal(1, newBalance);
            pstmt.setInt(2, accountId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}