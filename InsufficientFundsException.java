package edu.ithaca.dturnbull;

/*
 * Custom exception to handle overdrawing scenarios.
 */
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}