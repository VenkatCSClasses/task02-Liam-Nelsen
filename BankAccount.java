public class BankAccount {
    private String email;
    private double balance;

    /**
     * Constructor: Validates email and balance before initializing.
     */
    public BankAccount(String email, double initialBalance) {
        if (!isEmailValid(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (!isAmountValid(initialBalance)) {
            throw new IllegalArgumentException("Invalid initial balance. Amount must be positive and have at most 2 decimal places.");
        }
        this.email = email;
        this.balance = initialBalance;
    }

    // --- Accessors ---

    public double getBalance() {
        return balance;
    }

    public String getEmail() {
        return email;
    }

    // --- Core Methods ---

    public void deposit(double amount) {
        if (!isAmountValid(amount)) {
            throw new IllegalArgumentException("Deposit amount must be positive and have at most 2 decimal places.");
        }
        this.balance += amount;
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (!isAmountValid(amount)) {
            throw new IllegalArgumentException("Withdrawal amount must be positive and have at most 2 decimal places.");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal.");
        }
        this.balance -= amount;
    }

    public void transfer(double amount, BankAccount destination) throws InsufficientFundsException {
        if (destination == null) {
            throw new IllegalArgumentException("Destination account cannot be null.");
        }
        // This naturally validates the amount and checks for funds
        this.withdraw(amount);
        destination.deposit(amount);
    }

    // --- Validation Logic ---

    /**
     * Validates that an amount is positive and has no more than 2 decimal places.
     */
    public static boolean isAmountValid(double amount) {
        if (amount < 0) {
            return false;
        }
        // Multiply by 100 and check for leftover fractions to enforce 2 decimal places
        double cents = amount * 100;
        return Math.abs(cents - Math.round(cents)) < 0.0001;
    }

    /**
     * Validates email structure: local@domain.tld, no spaces, no double dots.
     */
    public static boolean isEmailValid(String email) {
        if (email == null || email.isEmpty()) return false;
        
        // 1. Check for spaces
        if (email.contains(" ")) return false;

        // 2. Check for exactly one '@'
        int firstAt = email.indexOf('@');
        int lastAt = email.lastIndexOf('@');
        if (firstAt == -1 || firstAt != lastAt) return false;

        // 3. Check for the dot used for the TLD
        int lastDotIndex = email.lastIndexOf('.');

        // 4. Structural rules:
        // - Local part must exist (firstAt > 0)
        // - Domain must exist between @ and . (lastDotIndex > firstAt + 1)
        // - TLD must exist after the last dot (lastDotIndex < length - 1)
        if (firstAt < 1 || lastDotIndex <= firstAt + 1 || lastDotIndex == email.length() - 1) {
            return false;
        }

        // 5. Check for double dots (common invalid format)
        if (email.contains("..")) {
            return false;
        }

        return true;
    }
}