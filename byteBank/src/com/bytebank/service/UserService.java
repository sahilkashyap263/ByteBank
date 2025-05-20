package com.bytebank.service;

import com.bytebank.dao.UserDAO;
import com.bytebank.model.User;

public class UserService {
    private UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    public boolean registerUser(String username, String password, String fullName, String email) {
        // Basic validation
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            fullName == null || fullName.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {
            return false;
        }
        
        // Check if username is already taken
        if (userDAO.getUserByUsername(username) != null) {
            return false;
        }
        
        // Create and save new user
        User user = new User(username, password, fullName, email);
        return userDAO.addUser(user);
    }
    
    public User authenticateUser(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        
        User user = userDAO.getUserByUsername(username);
        
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        
        return null;
    }
    
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }
    
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }
    
    public boolean updateUserDetails(User user) {
        return userDAO.updateUser(user);
    }
}