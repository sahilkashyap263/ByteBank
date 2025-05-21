package com.bytebank.ui;

import com.bytebank.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JButton registerButton;
    private JButton backButton;
    private UserService userService;

    public RegisterFrame() {
        userService = new UserService();
        initComponents();
    }

    private void initComponents() {
        setTitle("ByteBank - Register");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Colors and fonts
        Color primaryColor = new Color(52, 152, 219); // Blue
        Color bgColor = new Color(245, 245, 245);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Title label
        JLabel titleLabel = new JLabel("Register New Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(primaryColor);
        titlePanel.setPreferredSize(new Dimension(500, 60));
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Labels and Fields
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JLabel fullNameLabel = new JLabel("Full Name:");
        JLabel emailLabel = new JLabel("Email:");

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        fullNameField = new JTextField(20);
        emailField = new JTextField(20);

        for (JComponent field : new JComponent[]{usernameField, passwordField, confirmPasswordField, fullNameField, emailField}) {
            field.setFont(fieldFont);
            field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }

        for (JLabel label : new JLabel[]{usernameLabel, passwordLabel, confirmPasswordLabel, fullNameLabel, emailLabel}) {
            label.setFont(labelFont);
        }

        // Buttons
        registerButton = new JButton("Register");
        backButton = new JButton("Back to Login");

        for (JButton button : new JButton[]{registerButton, backButton}) {
            button.setFocusPainted(false);
            button.setBackground(primaryColor);
            button.setForeground(Color.blue);
            button.setFont(new Font("Segoe UI", Font.BOLD, 14));
            button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        }

        // Layout
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        formPanel.setBackground(bgColor);

        formPanel.add(usernameLabel); formPanel.add(usernameField);
        formPanel.add(passwordLabel); formPanel.add(passwordField);
        formPanel.add(confirmPasswordLabel); formPanel.add(confirmPasswordField);
        formPanel.add(fullNameLabel); formPanel.add(fullNameField);
        formPanel.add(emailLabel); formPanel.add(emailField);
        formPanel.add(backButton); formPanel.add(registerButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Event listeners
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToLogin();
            }
        });
    }

    private void handleRegistration() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String fullName = fullNameField.getText();
        String email = emailField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
            fullName.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = userService.registerUser(username, password, fullName, email);

        if (success) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            goBackToLogin();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Username might be taken.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void goBackToLogin() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        this.dispose();
    }
}
