import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void constructorEdgeCaseTest() {
        // Testing specific boundary for decimals and null inputs
        assertThrows(IllegalArgumentException.class, () -> new BankAccount(null, 100));
        
        // Precision boundary: 3 decimals should fail
        assertThrows(IllegalArgumentException.class, () -> new BankAccount("user@test.com", 50.001));
        
        // Valid edge case: very large initial balance
        BankAccount bigAccount = new BankAccount("whale@crypto.io", 9999999.99);
        assertEquals(9999999.99, bigAccount.getBalance(), 0.001);
    }

    @Test
    void isEmailValidVariationsTest() {
        // Valid: multiple dots in the local part are usually okay
        assertTrue(BankAccount.isEmailValid("first.last.name@domain.com"));
        
        // Valid: short TLDs
        assertTrue(BankAccount.isEmailValid("x@y.co"));

        // Invalid: No local part
        assertFalse(BankAccount.isEmailValid("@domain.com"));

        // Invalid: Space in email
        assertFalse(BankAccount.isEmailValid("user name@domain.com"));

        // Invalid: Multiple '@' symbols
        assertFalse(BankAccount.isEmailValid("user@name@domain.com"));
    }

    @Test
    void withdrawInsufficientFundsTest() throws InsufficientFundsException {
        BankAccount account = new BankAccount("student@university.edu", 50.00);

        // Attempting to withdraw slightly more than balance
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(50.01));

        // Attempting to withdraw from an already empty account
        account.withdraw(50.00);
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(0.01));
    }

    @Test
    void depositPrecisionTest() {
        BankAccount account = new BankAccount("save@money.com", 10.00);

        // Testing the smallest possible valid increment
        account.deposit(0.01);
        assertEquals(10.01, account.getBalance(), 0.001);

        // Valid: Whole numbers are fine
        account.deposit(100);
        assertEquals(110.01, account.getBalance(), 0.001);
    }

    @Test
    void transferStateVerificationTest() throws InsufficientFundsException {
        BankAccount source = new BankAccount("sender@pay.com", 500.00);
        BankAccount destination = new BankAccount("receiver@pay.com", 0.00);

        // Transfer a specific amount
        source.transfer(123.45, destination);

        // Verify state of BOTH accounts
        assertEquals(376.55, source.getBalance(), 0.001);
        assertEquals(123.45, destination.getBalance(), 0.001);
    }

    @Test
    void isAmountValidPrecisionTest() {
        // Boundary: Three decimal places vs two
        assertFalse(BankAccount.isAmountValid(0.001)); 
        assertTrue(BankAccount.isAmountValid(0.99));
        
        // Boundary: Large number with two decimals
        assertTrue(BankAccount.isAmountValid(1234567.89));
        
        // Zero is technically valid as an amount for validation logic
        assertTrue(BankAccount.isAmountValid(0.0));
    }

    // --- Additional tests to cover more edge cases and fix compile issues ---

    @Test
    void depositNegativeAmountThrows() {
        BankAccount acc = new BankAccount("ok@ex.com", 10.00);
        assertThrows(IllegalArgumentException.class, () -> acc.deposit(-1.00));
    }

    @Test
    void withdrawNegativeAmountThrows() {
        BankAccount acc = new BankAccount("ok@ex.com", 10.00);
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                acc.withdraw(-0.50);
            } catch (InsufficientFundsException e) {
                fail("Unexpected InsufficientFundsException for negative amount test");
            }
        });
    }

    @Test
    void transferToNullThrows() {
        BankAccount src = new BankAccount("a@b.com", 20.00);
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                src.transfer(5.00, null);
            } catch (InsufficientFundsException e) {
                // not expected here
                fail("Unexpected InsufficientFundsException when destination is null");
            }
        });
    }

    @Test
    void emailEdgeCasesTest() {
        // Trailing dot in domain
        assertFalse(BankAccount.isEmailValid("user@domain."));
        // No dot in domain
        assertFalse(BankAccount.isEmailValid("user@domain"));
        // Double dots anywhere are invalid
        assertFalse(BankAccount.isEmailValid("user..name@domain.com"));
        // Leading/trailing spaces invalid
        assertFalse(BankAccount.isEmailValid(" user@domain.com"));
        assertFalse(BankAccount.isEmailValid("user@domain.com "));
        // Single-char TLD is allowed by current logic
        assertTrue(BankAccount.isEmailValid("a@b.c"));
    }

    @Test
    void amountNegativeInvalidTest() {
        assertFalse(BankAccount.isAmountValid(-0.01));
        assertFalse(BankAccount.isAmountValid(-100.00));
    }

    @Test
    void depositAndWithdrawSequenceUpdatesBalance() throws InsufficientFundsException {
        BankAccount acc = new BankAccount("seq@test.com", 100.00);
        acc.deposit(25.50);
        assertEquals(125.50, acc.getBalance(), 0.001);
        acc.withdraw(25.50);
        assertEquals(100.00, acc.getBalance(), 0.001);
    }
}