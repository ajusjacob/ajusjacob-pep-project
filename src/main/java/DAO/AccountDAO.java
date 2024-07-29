package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    public boolean usernameExists(String username) {
        boolean exists = false;
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                exists = resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public Account verifyLogin(String username,String password){
        Account account = null;
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                account = new Account(
                    resultSet.getInt("account_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }

    public Account registerAccount(Account account) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO account (username, password) VALUES(?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            
            if (generatedKeys.next()) {
                account.setAccount_id(generatedKeys.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }

    public boolean usernameExistsById(int user_id) {
        boolean exists = false;
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM account WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                exists = resultSet.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }

}
