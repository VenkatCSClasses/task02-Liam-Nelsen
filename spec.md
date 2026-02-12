 Constructor, getBalance, getEmail, withdraw, deposit, transfer, isEmailValid, isAmountValid

This is a bank account project that has basic functionaility of a bank account such as a constructor, getBalance, getEmail, withdraw, deposit, transfer, isEmailValid, and isAmountValid methods.

Bank account class:

- Constructor(String email, double initialBalance): 
    Input: Takes an email address string and a starting balance double

    Calls isEmailValid() to check the validity of the input email
    Calls isAmountValid() to check the validity of the initial amount

    Sets the input email and balance as their respective attributes if valid

- getBalance():
    returns current balance of account

- getEmail()
    returns email of account

- deposit(double amount)
    Input: amount to transfer

    Checks if amount is valid and deposits amount into the bank account it is called upon

- withdraw(double amount)
    Input: amount to withdraw

    Checks if amount is valid, then if amount is less than current balance, it subtracts the amount from balance

- transfer(double amount, BankAccount destination)
    Input: amount to transfer
           destination bank account to transfer money to

    Calls withdraw method on own bank account, and deposits that amount into the destination bank account

- isAmountValid(double amount)
    Input: amount to check if it is valid
    
    Checks if amount is not a negative, and no more than two decimal places

- isEmailValid(String email)
    Input: email to check the validity of

    Checks if email has the right structure to be an email account