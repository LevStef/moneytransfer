public class Account {


    private double balance;
    private String userName;

    Account(String userName) {
        this.userName = userName;
        this.balance = 0;
    }

    double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    double updateBalance(double delta) {
        balance += delta;
        return balance;
    }

    public String getUserName() {
        return userName;
    }
}
