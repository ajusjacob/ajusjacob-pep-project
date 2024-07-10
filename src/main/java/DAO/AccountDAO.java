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
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM account WHERE username = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                exists = resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return exists;
    }

    public Account registerAccount(Account account){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO account (username, password) VALUES(?, ?)";
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();
            generatedKeys = preparedStatement.getGeneratedKeys();

            if(generatedKeys.next()){
                account.setAccount_id(generatedKeys.getInt(1));
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally{
            close(generatedKeys, preparedStatement, connection);
        }
        return account;
    }
    

    private void close(ResultSet rs, PreparedStatement ps, Connection con) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
