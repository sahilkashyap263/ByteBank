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
<img width="763" height="611" alt="image" src="https://github.com/user-attachments/assets/1132b5f2-d84d-4826-a0ef-6d631c154df2" />

### Registration
<img width="808" height="741" alt="image" src="https://github.com/user-attachments/assets/b17a3fc3-8223-4ea4-a9bc-5585cafcd2c6" />

### Dashboard
<img width="992" height="581" alt="image" src="https://github.com/user-attachments/assets/60f6bb94-c912-40c9-aaaa-5d38c31500f9" />

### Transfer Funds
<img width="992" height="581" alt="image" src="https://github.com/user-attachments/assets/4318bf28-9f26-424a-864f-35a54cc25ca9" />

### Transaction History
<img width="992" height="581" alt="image" src="https://github.com/user-attachments/assets/2eda749f-0bed-4f5e-8c8c-98738f38211d" />

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
