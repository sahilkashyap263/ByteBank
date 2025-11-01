# 💳 ByteBank - Banking Management System

A desktop banking application built with Java Swing and MySQL for managing accounts and transactions.

## Features

- User registration and login
- Create multiple accounts (Savings/Checking)
- Deposit, withdraw, and transfer funds
- View transaction history
- Real-time balance updates

## Screenshots

### Login Screen
![Login](screenshots/login.png)

### Registration
![Register](screenshots/register.png)

### Dashboard
![Dashboard](screenshots/dashboard.png)

### Transfer Funds
![Transfer](screenshots/transfer.png)

### Transaction History
![Transactions](screenshots/transactions.png)

## Tech Stack

- Java 8+
- Swing/AWT (GUI)
- MySQL (Database)
- JDBC (Database connectivity)

## Installation

### 1. Prerequisites
- JDK 8 or higher
- MySQL Server
- MySQL Connector/J

### 2. Configure Database
Update database credentials in `DBConnection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/bytebank";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";
```

### 3. Run
1. Import project into your IDE
2. Add MySQL Connector JAR to build path
3. Run the main class

## Usage

**Register**: Create a new user account  
**Login**: Access your dashboard  
**Create Account**: Add savings or checking accounts  
**Deposit/Withdraw**: Manage funds  
**Transfer**: Send money to other accounts  
**View History**: Check all transactions

## Project Structure
```
bytebank/
├── src/
│   └── com/bytebank/
│       ├── dao/         # Database operations
│       ├── model/       # Data models
│       ├── ui/          # GUI components
│       └── util/        # Utilities
├── database/
│   └── schema.sql
└── lib/
    └── mysql-connector.jar
```

## License

MIT License

---

Made with Java ☕
