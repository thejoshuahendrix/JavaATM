package atm;

import java.util.Scanner;

public class ATM {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //create bank
        Bank theBank = new Bank("Bank of GreenCamp");

        //add user
        User aUser = theBank.addUser("Josh", "Hend", "1234");

        //Add account for user
        Account newAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while (true) {
            //stay in login promt until successful login
            curUser = ATM.mainMenuPrompt(theBank, sc);

            //stay in main menu until user quits
            ATM.printUserMenu(curUser, sc);

        }

    }

    public static User mainMenuPrompt(Bank theBank, Scanner sc) {

        String userID;
        String pin;
        User authUser;

        //promt user for ID and pin until successful
        do {
            System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
            System.out.print("Enter user ID:");
            userID = sc.nextLine();
            System.out.print("Enter Pin:");
            pin = sc.nextLine();

            authUser = theBank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.println("Incorrect user ID/pin combination." + "Please try again.");
            }
        } while (authUser == null); // continue until successful login

        return authUser;
    }

    public static void printUserMenu(User theUser, Scanner sc) {

        //print user account summary
        theUser.printAccountSummary();

        int choice;

        do {

            //Display menu Choices and promp input
            System.out.printf("Welcome %s, what would you like to do?\n", theUser.getFirstName());
            System.out.println("\n 1) Show account transaction history");
            System.out.println(" 2) Withdrawal");
            System.out.println(" 3) Deposit");
            System.out.println(" 4) Transfer");
            System.out.println(" 5) Quit");
            System.out.println();
            System.out.print("Enter Choice: ");
            choice = sc.nextInt();

            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice. Please enter 1 - 5");

            }

        } while (choice < 1 || choice > 5);
        //Choice conditions
        switch (choice) {
            case 1:
                ATM.showTransHistory(theUser, sc);
                break;
            case 2:
                ATM.withdrawFunds(theUser, sc);
                break;
            case 3:
                ATM.depositFunds(theUser, sc);
                break;
            case 4:
                ATM.transferFunds(theUser, sc);
                break;
            case 5:
                sc.nextLine();
                break;
        }

        //Menu 
        if (choice != 5) {
            ATM.printUserMenu(theUser, sc);

        }
    }

    public static void showTransHistory(User theUser, Scanner sc) {
        int theAcct;

        do {
            System.out.printf("Enter the number of the account(1-%d)", theUser.numAccounts());
            theAcct = sc.nextInt() - 1;
            if (theAcct < 0 || theAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account number, try again");
            }
        } while (theAcct < 0 || theAcct >= theUser.numAccounts());
        theUser.printAcctTransHistory(theAcct);
    }

    public static void transferFunds(User theUser, Scanner sc) {

        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        do {
            System.out.printf("Enter the number (1-%d) of the account to transfer from:\n",theUser.numAccounts());
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account number, try again");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());

        acctBal = theUser.getAcctBalance(fromAcct);

        do {
            System.out.printf("Enter the number (1-%d) of the account to transfer to:\n",theUser.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account number, try again");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());
        do {
            System.out.printf("Enter the amount to transfer (max $%.02f): ", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero");
            } else if (amount > acctBal) {
                System.out.printf("Amount must be less than account balance of $%.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        //Transfer
        theUser.addAcctTransaction(fromAcct, -1 * amount, String.format("\nTransfer to account %s\n", theUser.getAcctUUID(toAcct)));
        theUser.addAcctTransaction(toAcct, amount, String.format("\nTransfer from account %s\n", theUser.getAcctUUID(fromAcct)));

    }
    public static void withdrawFunds(User theUser, Scanner sc){
        int fromAcct;
 
        double amount;
        double acctBal;
        String memo;

        do {
            System.out.printf("Enter the number (1-%d) of the account to withdraw from:\n",theUser.numAccounts());
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account number, try again");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());

        acctBal = theUser.getAcctBalance(fromAcct);
        do {
            System.out.printf("Enter the amount to withdraw (max $%.02f): ", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero");
            } else if (amount > acctBal) {
                System.out.printf("Amount must be less than account balance of $%.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);
        
        sc.nextLine();
        
        System.out.println("Enter a memo: ");
        memo = sc.nextLine();
        
        theUser.addAcctTransaction(fromAcct, -1*amount,memo);
    }
    public static void depositFunds(User theUser, Scanner sc){
        int toAcct;
 
        double amount;
        double acctBal;
        String memo;

        do {
            System.out.printf("Enter the number (1-%d) of the account to deposit to:\n",theUser.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account number, try again");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());

        acctBal = theUser.getAcctBalance(toAcct);
        do {
            System.out.printf("Enter the amount to deposit: ", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero");
            } 
        } while (amount < 0 );
        
        sc.nextLine();
        
        System.out.println("Enter a memo: ");
        memo = sc.nextLine() +"\n";
        
        theUser.addAcctTransaction(toAcct,amount,memo);
        
    }
}
