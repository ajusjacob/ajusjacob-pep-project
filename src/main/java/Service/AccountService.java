package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public boolean usernameExists(String username) {
        return accountDAO.usernameExists(username);
    }

    public Account verifyLogin(String username, String password){
        return accountDAO.verifyLogin(username,password);
    }

    public Account registerAccount(Account account) {
        return accountDAO.registerAccount(account);
    }

    public boolean usernameExistsById(int user_id) {
        return accountDAO.usernameExistsById(user_id);
    }
}