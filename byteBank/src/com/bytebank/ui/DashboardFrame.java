package com.bytebank.ui;

import com.bytebank.model.Account;
import com.bytebank.model.Transaction;
import com.bytebank.model.User;
import com.bytebank.service.AccountService;
import com.bytebank.service.TransactionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

public class DashboardFrame extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private User currentUser;
    private AccountService accountService;
    private TransactionService transactionService;
    private List<Account> userAccounts;
    private JComboBox<String> accountSelector;
    private JLabel balanceLabel;
    private JTable transactionTable;
    private DefaultTableModel transactionTableModel;
    
    public DashboardFrame(User user) {
        this.currentUser = user;
        this.accountService = new AccountService();
        this.transactionService = new TransactionService();
        this.userAccounts = accountService.getAccountsByUserId(user.getUserId());
        initComponents();
        loadAccountData();
    }
    
    private void initComponents() {
        setTitle("ByteBank - Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create components
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JLabel selectAccountLabel = new JLabel("Select Account:");
        accountSelector = new JComboBox<>();
        
        JLabel balanceTextLabel = new JLabel("Current Balance:");
        balanceLabel = new JLabel("₹ 0.00");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JButton refreshButton = new JButton("Refresh");
        JButton logoutButton = new JButton("Logout");
        JButton createAccountButton = new JButton("Create New Account");
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton transferButton = new JButton("Transfer");
        
        // Transaction table
        String[] columnNames = {"Date", "Type", "Amount", "Description", "Balance"};
        transactionTableModel = new DefaultTableModel(columnNames, 0);
        transactionTable = new JTable(transactionTableModel);
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        
        // Layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.add(welcomeLabel);
        
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutButton);
        
        topPanel.add(userPanel, BorderLayout.WEST);
        topPanel.add(logoutPanel, BorderLayout.EAST);
        
        JPanel accountPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        accountPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        accountPanel.add(selectAccountLabel);
        accountPanel.add(balanceTextLabel);
        accountPanel.add(new JLabel(""));
        accountPanel.add(accountSelector);
        accountPanel.add(balanceLabel);
        accountPanel.add(refreshButton);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(createAccountButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(transferButton);
        
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(topPanel, BorderLayout.NORTH);
        northPanel.add(accountPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Event listeners
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
        
        accountSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedAccount();
            }
        });
        
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewAccount();
            }
        });
        
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDepositDialog();
            }
        });
        
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showWithdrawDialog();
            }
        });
        
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTransferDialog();
            }
        });
    }
    
    private void loadAccountData() {
        accountSelector.removeAllItems();
        
        if (userAccounts != null && !userAccounts.isEmpty()) {
            for (Account account : userAccounts) {
                accountSelector.addItem(account.getAccountNumber() + " (" + account.getAccountType() + ")");
            }
            updateSelectedAccount();
        } else {
            balanceLabel.setText("₹0.00");
            transactionTableModel.setRowCount(0);
            
            // Ask if user wants to create an account if none exists
            int choice = JOptionPane.showConfirmDialog(this, 
                "No accounts found. Would you like to create a new account?", 
                "Create Account", 
                JOptionPane.YES_NO_OPTION);
                
            if (choice == JOptionPane.YES_OPTION) {
                createNewAccount();
            }
        }
    }
    
    private void updateSelectedAccount() {
        int selectedIndex = accountSelector.getSelectedIndex();
        
        if (selectedIndex >= 0 && selectedIndex < userAccounts.size()) {
            Account selectedAccount = userAccounts.get(selectedIndex);
            balanceLabel.setText("" + selectedAccount.getBalance().toString());
            loadTransactions(selectedAccount.getAccountId());
        }
    }
    
    private void loadTransactions(int accountId) {
        transactionTableModel.setRowCount(0);
        
        List<Transaction> transactions = transactionService.getTransactionHistory(accountId);
        
        if (transactions != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            BigDecimal runningBalance = getSelectedAccount().getBalance();
            
            for (Transaction transaction : transactions) {
                String date = dateFormat.format(transaction.getTransactionDate());
                String type = transaction.getTransactionType().toString();
                String amount = "₹" + transaction.getAmount().toString();
                String description = transaction.getDescription();
                
                // Calculate running balance based on transaction type
                BigDecimal balanceAfterTransaction;
                
                switch (transaction.getTransactionType()) {
                    case DEPOSIT:
                    case TRANSFER_IN:
                        balanceAfterTransaction = runningBalance;
                        runningBalance = runningBalance.subtract(transaction.getAmount());
                        break;
                    case WITHDRAWAL:
                    case TRANSFER_OUT:
                        balanceAfterTransaction = runningBalance;
                        runningBalance = runningBalance.add(transaction.getAmount());
                        break;
                    default:
                        balanceAfterTransaction = runningBalance;
                        break;
                }
                
                String balance = "₹" + balanceAfterTransaction.toString();
                
                transactionTableModel.addRow(new Object[]{date, type, amount, description, balance});
            }
        }
    }
    
    private Account getSelectedAccount() {
        int selectedIndex = accountSelector.getSelectedIndex();
        
        if (selectedIndex >= 0 && selectedIndex < userAccounts.size()) {
            return userAccounts.get(selectedIndex);
        }
        
        return null;
    }
    
    private void refreshData() {
        userAccounts = accountService.getAccountsByUserId(currentUser.getUserId());
        loadAccountData();
    }
    
    private void logout() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        this.dispose();
    }
    
    private void createNewAccount() {
        Object[] accountTypes = {Account.AccountType.CHECKING, Account.AccountType.SAVINGS};
        Account.AccountType selectedType = (Account.AccountType) JOptionPane.showInputDialog(
            this,
            "Select account type:",
            "Create New Account",
            JOptionPane.QUESTION_MESSAGE,
            null,
            accountTypes,
            accountTypes[0]
        );
        
        if (selectedType != null) {
            Account newAccount = accountService.createAccount(currentUser.getUserId(), selectedType);
            
            if (newAccount != null) {
                JOptionPane.showMessageDialog(this, 
                    "Account created successfully!\nAccount Number: " + newAccount.getAccountNumber(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to create account.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showDepositDialog() {
        Account selectedAccount = getSelectedAccount();
        
        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(this, "Please select an account first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String amountStr = JOptionPane.showInputDialog(this, "Enter deposit amount:", "Deposit", JOptionPane.QUESTION_MESSAGE);
        
        if (amountStr != null && !amountStr.trim().isEmpty()) {
            try {
                BigDecimal amount = new BigDecimal(amountStr);
                
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be greater than zero", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String description = JOptionPane.showInputDialog(this, "Enter description (optional):", "Deposit", JOptionPane.QUESTION_MESSAGE);
                
                if (description == null) {
                    description = "Deposit";
                }
                
                boolean success = transactionService.recordDeposit(selectedAccount.getAccountId(), amount, description);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Deposit successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "Deposit failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showWithdrawDialog() {
        Account selectedAccount = getSelectedAccount();
        
        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(this, "Please select an account first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String amountStr = JOptionPane.showInputDialog(this, "Enter withdrawal amount:", "Withdraw", JOptionPane.QUESTION_MESSAGE);
        
        if (amountStr != null && !amountStr.trim().isEmpty()) {
            try {
                BigDecimal amount = new BigDecimal(amountStr);
                
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be greater than zero", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (amount.compareTo(selectedAccount.getBalance()) > 0) {
                    JOptionPane.showMessageDialog(this, "Insufficient funds", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String description = JOptionPane.showInputDialog(this, "Enter description (optional):", "Withdraw", JOptionPane.QUESTION_MESSAGE);
                
                if (description == null) {
                    description = "Withdrawal";
                }
                
                boolean success = transactionService.recordWithdrawal(selectedAccount.getAccountId(), amount, description);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Withdrawal successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "Withdrawal failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showTransferDialog() {
        Account selectedAccount = getSelectedAccount();
        
        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(this, "Please select an account first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get destination account
        String accountNumber = JOptionPane.showInputDialog(this, "Enter recipient account number:", "Transfer", JOptionPane.QUESTION_MESSAGE);
        
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return;
        }
        
        Account recipientAccount = accountService.getAccountByAccountNumber(accountNumber);
        
        if (recipientAccount == null) {
            JOptionPane.showMessageDialog(this, "Recipient account not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (recipientAccount.getAccountId() == selectedAccount.getAccountId()) {
            JOptionPane.showMessageDialog(this, "Cannot transfer to the same account", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get amount
        String amountStr = JOptionPane.showInputDialog(this, "Enter transfer amount:", "Transfer", JOptionPane.QUESTION_MESSAGE);
        
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return;
        }
        
        try {
            BigDecimal amount = new BigDecimal(amountStr);
            
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than zero", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (amount.compareTo(selectedAccount.getBalance()) > 0) {
                JOptionPane.showMessageDialog(this, "Insufficient funds", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get description
            String description = JOptionPane.showInputDialog(this, "Enter description (optional):", "Transfer", JOptionPane.QUESTION_MESSAGE);
            
            if (description == null) {
                description = "Transfer to account " + accountNumber;
            }
            
            boolean success = transactionService.transferFunds(
                selectedAccount.getAccountId(), 
                recipientAccount.getAccountId(),
                amount,
                description
            );
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Transfer successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Transfer failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}