package atm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User {

    private String firstName;
    private String lastName;
    private String uuid;
    private byte pinHash[];
    private ArrayList<Account> accounts;

    public User(String firstName, String lastName, String pin, Bank theBank) {
        this.firstName = firstName;
        this.lastName = lastName;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("err, NoSuch");
            System.exit(1);
        }

        this.uuid = theBank.getNewUserUUID();

        this.accounts = new ArrayList<Account>();

        System.out.printf("New user %s, %s with ID %s created", lastName, firstName, this.uuid);
    }

    public void addAccount(Account anAcct) {
        this.accounts.add(anAcct);

    }

    public String getUUID() {
        return this.uuid;
    }

    public boolean validatePin(String aPin) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(aPin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("err, NoSuch");
            System.exit(1);;
        }
        return false;

    }

    public String getFirstName() {
        return this.firstName;
    }

    public void printAccountSummary() {
        System.out.printf("\n\n%s's account summary:\n", this.firstName);
        for (int a = 0; a < this.accounts.size(); a++) {
            System.out.printf("%d) %s\n", a+1, this.accounts.get(a).getSummaryLine());
        }
    }

    public int numAccounts() {
        return this.accounts.size();
    }

    public void printAcctTransHistory(int acctIndex) {
        this.accounts.get(acctIndex).printTransHistory();
    }
    public double getAcctBalance(int acctIndex){
        return this.accounts.get(acctIndex).getBalance();
    }
    public String getAcctUUID(int acctIndex){
        return this.accounts.get(acctIndex).getUUID();
    }
    
    public void addAcctTransaction(int acctIndex, double amount, String memo){
        this.accounts.get(acctIndex).addTransaction(amount, memo);
    }
}
