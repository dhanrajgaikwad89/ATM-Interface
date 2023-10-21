import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

class User {
    private String userId;
    private String pin;
    private double balance;
    private List<String> transactionHistory;

    public User(String userId, String pin) {
        this.userId = userId;
        this.pin = pin;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add("Deposited: $" + amount);
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            transactionHistory.add("Withdrawn: $" + amount);
            return true;
        }
        return false;
    }

    public void transfer(User recipient, double amount) {
        if (withdraw(amount)) {
            recipient.deposit(amount);
            transactionHistory.add("Transferred: $" + amount + " to " + recipient.getUserId());
        }
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }
}

public class ATM {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create user accounts (you can expand this with more users)
        User user1 = new User("user123", "1234");
        User user2 = new User("user456", "5678");

        // Simulate ATM interface
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        User currentUser = authenticateUser(userId, pin, user1, user2);

        if (currentUser != null) {
            System.out.println("Welcome, " + userId + "!");
            boolean isRunning = true;
            while (isRunning) {
                System.out.println("\nATM Menu:");
                System.out.println("1. Withdraw");
                System.out.println("2. Deposit");
                System.out.println("3. Transfer");
                System.out.println("4. Transaction History");
                System.out.println("5. Quit");
                System.out.print("Enter your choice (1/2/3/4/5): ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter withdrawal amount: $");
                        double withdrawAmount = scanner.nextDouble();
                        if (currentUser.withdraw(withdrawAmount)) {
                            System.out.println("Withdrawal successful.");
                        } else {
                            System.out.println("Insufficient funds.");
                        }
                        break;
                    case 2:
                        System.out.print("Enter deposit amount: $");
                        double depositAmount = scanner.nextDouble();
                        currentUser.deposit(depositAmount);
                        System.out.println("Deposit successful.");
                        break;
                    case 3:
                        System.out.print("Enter recipient's user ID: ");
                        String recipientId = scanner.next();
                        User recipient = (user1.getUserId().equals(recipientId)) ? user1 : user2;
                        if (recipient != currentUser) {
                            System.out.print("Enter transfer amount: $");
                            double transferAmount = scanner.nextDouble();
                            currentUser.transfer(recipient, transferAmount);
                            System.out.println("Transfer successful.");
                        } else {
                            System.out.println("Cannot transfer to the same account.");
                        }
                        break;
                    case 4:
                        List<String> transactions = currentUser.getTransactionHistory();
                        System.out.println("Transaction History for " + currentUser.getUserId() + ":");
                        for (String transaction : transactions) {
                            System.out.println(transaction);
                        }
                        break;
                    case 5:
                        System.out.println("Goodbye!");
                        isRunning = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please select 1, 2, 3, 4, or 5.");
                }
            }
        } else {
            System.out.println("Authentication failed. Exiting.");
        }

        scanner.close();
    }

    private static User authenticateUser(String userId, String pin, User... users) {
        for (User user : users) {
            if (user.getUserId().equals(userId) && user.getPin().equals(pin)) {
                return user;
            }
        }
        return null; // Authentication failed
    }
}
