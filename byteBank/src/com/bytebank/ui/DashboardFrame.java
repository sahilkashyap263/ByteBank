package com.bytebank.ui;

import com.bytebank.model.Account;
import com.bytebank.model.Transaction;
import com.bytebank.model.User;
import com.bytebank.service.AccountService;
import com.bytebank.service.TransactionService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
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
    
    // Modern UI colors - matching LoginFrame
    private final Color PRIMARY_COLOR = new Color(33, 150, 243); // Material Blue
    private final Color ACCENT_COLOR = new Color(255, 152, 0);   // Material Orange
    private final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    private final Color TEXT_COLOR = new Color(33, 33, 33);
    private final Color LIGHT_GRAY = new Color(240, 240, 240);
    private final Color BORDER_COLOR = new Color(224, 224, 224);
    
    // Gradient colors
    private final Color GRADIENT_START = new Color(25, 118, 210); // Darker blue
    private final Color GRADIENT_END = new Color(66, 165, 245);   // Lighter blue
    
    // Orange gradient for accent
    private final Color ACCENT_GRADIENT_START = new Color(245, 124, 0); // Darker orange
    private final Color ACCENT_GRADIENT_END = new Color(255, 167, 38);  // Lighter orange
    
    // Modern UI fonts - matching LoginFrame
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private final Font BALANCE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    
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
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set background color for the frame
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Create the header panel with welcome message and logout button
        JPanel headerPanel = createHeaderPanel();
        
        // Create the account information panel
        JPanel accountInfoPanel = createAccountInfoPanel();
        
        // Create the action buttons panel
        JPanel actionPanel = createActionPanel();
        
        // Create the transactions panel
        JPanel transactionsPanel = createTransactionsPanel();
        
        // Main panel to hold everything
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 25, 25, 25));
        
        // Create a panel for the top sections
        JPanel topSection = new JPanel();
        topSection.setLayout(new BorderLayout(0, 15));
        topSection.setBackground(BACKGROUND_COLOR);
        topSection.add(headerPanel, BorderLayout.NORTH);
        topSection.add(accountInfoPanel, BorderLayout.CENTER);
        topSection.add(actionPanel, BorderLayout.SOUTH);
        
        mainPanel.add(topSection, BorderLayout.NORTH);
        mainPanel.add(transactionsPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Setup event listeners
        setupEventListeners();
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(0, 0, 15, 0)));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName());
        welcomeLabel.setFont(TITLE_FONT);
        welcomeLabel.setForeground(PRIMARY_COLOR);
        
        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton, Color.WHITE, PRIMARY_COLOR);
        logoutButton.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1, true));
        logoutButton.setPreferredSize(new Dimension(100, 36));
        
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createAccountInfoPanel() {
        JPanel accountInfoPanel = new JPanel();
        accountInfoPanel.setLayout(new BorderLayout(15, 0));
        accountInfoPanel.setBackground(BACKGROUND_COLOR);
        accountInfoPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        // Left side - Account selector
        JPanel accountSelectorPanel = new JPanel(new BorderLayout(0, 8));
        accountSelectorPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel selectAccountLabel = new JLabel("Select Account");
        selectAccountLabel.setFont(LABEL_FONT);
        selectAccountLabel.setForeground(TEXT_COLOR);
        
        accountSelector = new JComboBox<>();
        accountSelector.setFont(LABEL_FONT);
        accountSelector.setBackground(Color.WHITE);
        accountSelector.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        accountSelector.setPreferredSize(new Dimension(250, 40));
        
        accountSelectorPanel.add(selectAccountLabel, BorderLayout.NORTH);
        accountSelectorPanel.add(accountSelector, BorderLayout.CENTER);
        
        // Center - Balance display (moved from right to center)
        JPanel balancePanel = new JPanel();
        balancePanel.setLayout(new BoxLayout(balancePanel, BoxLayout.Y_AXIS));
        balancePanel.setBackground(BACKGROUND_COLOR);
        balancePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel balanceTextLabel = new JLabel("Current Balance");
        balanceTextLabel.setFont(LABEL_FONT);
        balanceTextLabel.setForeground(TEXT_COLOR);
        balanceTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        balanceLabel = new JLabel("₹ 0.00");
        balanceLabel.setFont(BALANCE_FONT);
        balanceLabel.setForeground(ACCENT_COLOR);
        balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        balancePanel.add(balanceTextLabel);
        balancePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        balancePanel.add(balanceLabel);
        
        // Right side - Enhanced refresh button
        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshPanel.setBackground(BACKGROUND_COLOR);
        
        // Create a custom animated refresh button
        JButton refreshButton = new JButton("↻ Refresh") {
            private boolean isAnimating = false;
            private Timer animationTimer;
            private float rotation = 0f;
            
            {
                // Set up animation timer
                animationTimer = new Timer(20, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        rotation += 10f;
                        if (rotation >= 360f) {
                            rotation = 0f;
                            isAnimating = false;
                            animationTimer.stop();
                        }
                        repaint();
                    }
                });
                
                // Add animation start on click
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!isAnimating) {
                            isAnimating = true;
                            animationTimer.start();
                        }
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Create gradient paint
                GradientPaint gradient = new GradientPaint(
                    0, 0, GRADIENT_START,
                    0, h, GRADIENT_END);
                
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, w, h, 10, 10);
                
                // Draw text with rotation effect if animating
                g2d.setColor(Color.WHITE);
                g2d.setFont(BUTTON_FONT);
                
                if (isAnimating) {
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth("↻");
                    int textHeight = fm.getHeight();
                    
                    // Save the current transform
                    AffineTransform oldTransform = g2d.getTransform();
                    
                    // Set up rotation around the center of the "↻" symbol
                    g2d.translate(20 + textWidth/2, h/2);
                    g2d.rotate(Math.toRadians(rotation));
                    g2d.translate(-textWidth/2, 0);
                    
                    // Draw just the refresh symbol with rotation
                    g2d.drawString("↻", 0, fm.getAscent()/2);
                    
                    // Restore the original transform
                    g2d.setTransform(oldTransform);
                    
                    // Draw the "Refresh" text without rotation
                    g2d.drawString("Refresh", 30, (h - textHeight) / 2 + fm.getAscent());
                } else {
                    // Draw normal text when not animating
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(getText());
                    int textHeight = fm.getHeight();
                    
                    g2d.drawString(getText(), 
                        (w - textWidth) / 2, 
                        (h - textHeight) / 2 + fm.getAscent());
                }
                
                g2d.dispose();
            }
        };
        
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(BUTTON_FONT);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setContentAreaFilled(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.setPreferredSize(new Dimension(110, 40)); // Slightly larger for better visibility
        
        refreshPanel.add(refreshButton);
        
        // Add components to account info panel
        accountInfoPanel.add(accountSelectorPanel, BorderLayout.WEST);
        accountInfoPanel.add(balancePanel, BorderLayout.CENTER); // Centered
        accountInfoPanel.add(refreshPanel, BorderLayout.EAST);
        
        return accountInfoPanel;
    }
    
    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Increased spacing
        actionPanel.setBackground(BACKGROUND_COLOR);
        actionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(5, 0, 15, 0)));
        
        // Button with gradient background
        JButton createAccountButton = createGradientButton("Create New Account", GRADIENT_START, GRADIENT_END);
        JButton depositButton = createGradientButton("Deposit", ACCENT_GRADIENT_START, ACCENT_GRADIENT_END);
        JButton withdrawButton = createGradientButton("Withdraw", GRADIENT_START, GRADIENT_END);
        JButton transferButton = createGradientButton("Transfer", GRADIENT_START, GRADIENT_END);
        
        // Set fixed width for buttons to ensure text is fully visible
        createAccountButton.setPreferredSize(new Dimension(150, 40));
        depositButton.setPreferredSize(new Dimension(100, 40));
        withdrawButton.setPreferredSize(new Dimension(100, 40));
        transferButton.setPreferredSize(new Dimension(100, 40));
        
        actionPanel.add(createAccountButton);
        actionPanel.add(depositButton);
        actionPanel.add(withdrawButton);
        actionPanel.add(transferButton);
        
        return actionPanel;
    }
    
    // Custom method to create gradient buttons
    private JButton createGradientButton(String text, final Color gradientStart, final Color gradientEnd) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Create gradient paint
                GradientPaint gradient = new GradientPaint(
                    0, 0, gradientStart,
                    0, h, gradientEnd);
                
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, w, h, 10, 10);
                
                // Draw text
                g2d.setColor(Color.WHITE);
                g2d.setFont(BUTTON_FONT);
                
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getHeight();
                
                g2d.drawString(getText(), 
                    (w - textWidth) / 2, 
                    (h - textHeight) / 2 + fm.getAscent());
                
                g2d.dispose();
            }
        };
        
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private JPanel createTransactionsPanel() {
        JPanel transactionsPanel = new JPanel(new BorderLayout(0, 10));
        transactionsPanel.setBackground(BACKGROUND_COLOR);
        
        // Transaction section header
        JLabel transactionsLabel = new JLabel("Recent Transactions");
        transactionsLabel.setFont(SUBTITLE_FONT);
        transactionsLabel.setForeground(TEXT_COLOR);
        transactionsLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Transaction table
        String[] columnNames = {"Date", "Type", "Amount", "Description", "Balance"};
        transactionTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        transactionTable = new JTable(transactionTableModel);
        transactionTable.setFont(TABLE_FONT);
        transactionTable.setRowHeight(30);
        transactionTable.setShowGrid(false);
        transactionTable.setIntercellSpacing(new Dimension(0, 0));
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setSelectionBackground(new Color(232, 240, 254));
        transactionTable.setSelectionForeground(TEXT_COLOR);
        
        // Style the table header
        JTableHeader header = transactionTable.getTableHeader();
        header.setFont(TABLE_HEADER_FONT);
        header.setBackground(LIGHT_GRAY);
        header.setForeground(TEXT_COLOR);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        
        // Create a scroll pane with custom border
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        transactionsPanel.add(transactionsLabel, BorderLayout.NORTH);
        transactionsPanel.add(scrollPane, BorderLayout.CENTER);
        
        return transactionsPanel;
    }
    
    private void styleButton(JButton button, Color background, Color foreground) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(background == Color.WHITE || background == BACKGROUND_COLOR);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    }
    
    private void setupEventListeners() {
        // Get the buttons by traversing the component hierarchy
        Component[] components = getContentPane().getComponents();
        JButton logoutButton = findButtonByText(components, "Logout");
        JButton refreshButton = findButtonByText(components, "↻ Refresh");
        JButton createAccountButton = findButtonByText(components, "Create New Account");
        JButton depositButton = findButtonByText(components, "Deposit");
        JButton withdrawButton = findButtonByText(components, "Withdraw");
        JButton transferButton = findButtonByText(components, "Transfer");
        
        // Add listeners
        if (logoutButton != null) {
            logoutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    logout();
                }
            });
        }
        
        if (refreshButton != null) {
            refreshButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    refreshData();
                }
            });
        }
        
        accountSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedAccount();
            }
        });
        
        if (createAccountButton != null) {
            createAccountButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createNewAccount();
                }
            });
        }
        
        if (depositButton != null) {
            depositButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showDepositDialog();
                }
            });
        }
        
        if (withdrawButton != null) {
            withdrawButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showWithdrawDialog();
                }
            });
        }
        
        if (transferButton != null) {
            transferButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showTransferDialog();
                }
            });
        }
    }
    
    // Helper method to find buttons by text
    private JButton findButtonByText(Component[] components, String text) {
        for (Component component : components) {
            if (component instanceof JButton && ((JButton) component).getText().equals(text)) {
                return (JButton) component;
            } else if (component instanceof Container) {
                JButton button = findButtonByText(((Container) component).getComponents(), text);
                if (button != null) {
                    return button;
                }
            }
        }
        return null;
    }
    
    // Create custom dialog for numeric input
    private JDialog createInputDialog(String title, String message) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(LABEL_FONT);
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        inputField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton cancelButton = new JButton("Cancel");
        styleButton(cancelButton, Color.WHITE, TEXT_COLOR);
        cancelButton.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1, true));
        
        JButton confirmButton = new JButton("Confirm");
        styleButton(confirmButton, PRIMARY_COLOR, Color.WHITE);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);
        
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(inputField);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(buttonPanel);
        
        dialog.add(contentPanel, BorderLayout.CENTER);
        
        return dialog;
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
            balanceLabel.setText("₹" + selectedAccount.getBalance().toString());
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
