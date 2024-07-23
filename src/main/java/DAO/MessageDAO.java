package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.MenuElement;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<>();
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Message message = new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch")
                );  
                messages.add(message);              
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return messages;
    }

    public Message addMessage(Message message){
        Message addedMessage = null;
        try {
            PreparedStatement preparedStatement = null;
            ResultSet generatedKeys = null;
            Connection connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql, preparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int message_id = generatedKeys.getInt(1);
                    addedMessage = new Message(
                        message_id,
                        message.getPosted_by(),
                        message.getMessage_text(),
                        message.getTime_posted_epoch()
                    );
                }
            }
            
        } catch (Exception e) {
            // TODO: handle exception
        }
        return addedMessage;
        
    }

    public Message getMessagesbyId(int message_id){
        Message message = null;
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                message = new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch")
                );
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return message;
    }

    public Message deletemessageId(int message_id){
        Message messageToDelete = null;
        try {
            Connection connection = ConnectionUtil.getConnection();
            String selectSql ="SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setInt(1, message_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                messageToDelete = new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch")
                );
            }

            if (messageToDelete != null) {
                String deleteSql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement preparedStatement2 = connection.prepareStatement(deleteSql);
                preparedStatement.setInt(1, message_id);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return messageToDelete;
    }

    public Message getMessagebyId(int messageId){
        Message message = null;
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                message = new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch")
                );
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return message;
    }

    public boolean updateMessageText(int messageId, String newMessageText){
        boolean isUpdated = false;
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newMessageText);
            preparedStatement.setInt(2, messageId);
            int affectedRows = preparedStatement.executeUpdate();
            isUpdated = affectedRows > 0;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return isUpdated;
    }















}
