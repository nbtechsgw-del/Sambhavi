import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BankingException extends Exception {
    public BankingException(String message) {
        super(message);
    }
}

class Transaction {
    private final String type;
    private final double amount;
    private final double balanceAfterTransaction;

    public Transaction(String type, double amount, double balanceAfterTransaction) {
        this.type = type;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    @Override
    public String toString() {
        return String.format("%-10s Amount: %.2f | Balance: %.2f",
                type, amount, balanceAfterTransaction);
    }
}

class Account {
    private final int accountNumber;
    private final String accountHolder;
    private double balance;
    private final List<Transaction> transactionHistory;

    public Account(int accountNumber, String accountHolder, double initialBalance) throws BankingException {
        if (initialBalance < 0) {
            throw new BankingException("Initial balance cannot be negative.");
        }

        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();

        if (initialBalance > 0) {
            addTransaction("Created", initialBalance);
        }
    }

    public void deposit(double amount) throws BankingException {
        validateAmount(amount);
        balance += amount;
        addTransaction("Deposit", amount);
    }

    public void withdraw(double amount) throws BankingException {
        validateAmount(amount);
        if (amount > balance) {
            throw new BankingException("Insufficient balance for withdrawal.");
        }

        balance -= amount;
        addTransaction("Withdraw", amount);
    }

    public void displayAccountDetails() {
        System.out.println("Account Number : " + accountNumber);
        System.out.println("Account Holder : " + accountHolder);
        System.out.printf("Current Balance: %.2f%n", balance);
    }

    public void displayTransactionHistory() {
        System.out.println("\nTransaction History");
        System.out.println("-------------------");

        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions available.");
            return;
        }

        for (Transaction transaction : transactionHistory) {
            System.out.println(transaction);
        }
    }

    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }

    private void validateAmount(double amount) throws BankingException {
        if (amount <= 0) {
            throw new BankingException("Amount must be greater than zero.");
        }
    }

    private void addTransaction(String type, double amount) {
        transactionHistory.add(new Transaction(type, amount, balance));
    }
}

public class banking {
    public static void main(String[] args) {
        try {
            Account account = new Account(1001, "Shambhavi", 5000);

            System.out.println("Account created successfully.\n");
            account.displayAccountDetails();

            account.deposit(1500);
            account.withdraw(2000);

            try {
                account.withdraw(10000);
            } catch (BankingException e) {
                System.out.println("\nHandled exception: " + e.getMessage());
            }

            try {
                account.deposit(-100);
            } catch (BankingException e) {
                System.out.println("Handled exception: " + e.getMessage());
            }

            System.out.println();
            account.displayAccountDetails();
            account.displayTransactionHistory();
        } catch (BankingException e) {
            System.out.println("Error while creating account: " + e.getMessage());
        }
    }
}

