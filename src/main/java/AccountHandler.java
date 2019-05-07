import java.util.ArrayList;
import java.util.List;

public class AccountHandler {
    private List<Account> accounts = new ArrayList<>();
    private int accountCounter = 0;

    /**
     * creates a new account. returns accountID.
     */
    public int createAccount(String userName) {
        Account account = new Account(userName);
        accounts.add(account);
        return accountCounter++;
    }

    /**
     * returns account balance
     */
    public double getBalance(int accountID) throws UnknownAccountException {
        if (accountID >= accounts.size() || accountID < 0) {
            throw new UnknownAccountException("Account does not exist");
        }
        return accounts.get(accountID).getBalance();
    }

    /**
     * updates balance of accountID by the delta. Use negative number to withdraw funds.
     * returns new account balance.
     */
    public double updateBalance(int accountID, double delta) throws OverdrawException {
        if (accounts.get(accountID).getBalance() + delta < 0) {
            throw new OverdrawException("Insufficient funds");
        }
        return accounts.get(accountID).updateBalance(delta);
    }

    /**
     * Transfers money from accountSend to accountReceive. funds must be positive number
     */
    public void transferFunds(int accountSend, int accountReceive, double funds) throws OverdrawException {
        if (accounts.get(accountSend).getBalance() - funds < 0) {
            throw new OverdrawException("Insufficient funds");
        }
        accounts.get(accountSend).updateBalance(-funds);
        accounts.get(accountReceive).updateBalance(funds);
    }
}
