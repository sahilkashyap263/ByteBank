package com.bytebank.ui;

import com.bytebank.model.User;
import com.bytebank.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private UserService userService;
    
    // Modern UI colors
    private final Color PRIMARY_COLOR = new Color(33, 150, 243); // Material Blue
    private final Color ACCENT_COLOR = new Color(255, 152, 0);   // Material Orange
    private final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    private final Color TEXT_COLOR = new Color(33, 33, 33);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    
    public LoginFrame() {
        userService = new UserService();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("ByteBank - Login");
        setSize(480, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set background color for the frame
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Create the logo/banner panel
        JPanel bannerPanel = createBannerPanel();
        
        // Create form panel with modern styling
        JPanel formPanel = createFormPanel();
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Main layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 30, 30, 30));
        
        mainPanel.add(bannerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Add event listeners
        setupEventListeners();
    }
    
    private JPanel createBannerPanel() {
        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Welcome to ByteBank");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel subtitleLabel = new JLabel("Sign in to your account");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_COLOR);
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        bannerPanel.add(titleLabel, BorderLayout.NORTH);
        bannerPanel.add(subtitleLabel, BorderLayout.CENTER);
        bannerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        return bannerPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(2, 1, 0, 15));
        formPanel.setBackground(BACKGROUND_COLOR);
        
        // Username field with panel
        JPanel usernamePanel = new JPanel(new BorderLayout(0, 5));
        usernamePanel.setBackground(BACKGROUND_COLOR);
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(LABEL_FONT);
        usernameLabel.setForeground(TEXT_COLOR);
        
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        usernameField.setBackground(Color.WHITE);
        
        usernamePanel.add(usernameLabel, BorderLayout.NORTH);
        usernamePanel.add(usernameField, BorderLayout.CENTER);
        
        // Password field with panel
        JPanel passwordPanel = new JPanel(new BorderLayout(0, 5));
        passwordPanel.setBackground(BACKGROUND_COLOR);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(LABEL_FONT);
        passwordLabel.setForeground(TEXT_COLOR);
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        passwordField.setBackground(Color.WHITE);
        
        passwordPanel.add(passwordLabel, BorderLayout.NORTH);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        
        formPanel.add(usernamePanel);
        formPanel.add(passwordPanel);
        
        return formPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        loginButton = new JButton("Login");
        styleButton(loginButton, PRIMARY_COLOR, Color.WHITE);
        
        registerButton = new JButton("Register");
        styleButton(registerButton, Color.WHITE, PRIMARY_COLOR);
        registerButton.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1, true));
        
        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);
        
        return buttonPanel;
    }
    
    private void styleButton(JButton button, Color background, Color foreground) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(background == Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    private void setupEventListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterForm();
            }
        });
    }
    
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        User user = userService.authenticateUser(username, password);
        
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            openDashboard(user);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showRegisterForm() {
        RegisterFrame registerFrame = new RegisterFrame();
        registerFrame.setVisible(true);
        this.dispose();
    }
    
    private void openDashboard(User user) {
        DashboardFrame dashboard = new DashboardFrame(user);
        dashboard.setVisible(true);
        this.dispose();
    }
    
    public static void main(String[] args) {
        try {
            // Set look and feel to system default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Improve button and component rendering
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("ProgressBar.arc", 10);
            UIManager.put("TextComponent.arc", 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}
