package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message addMessage(Message message){
        return messageDAO.addMessage(message);
    }

    public Message getMessagesbyId(int messageId){
        return messageDAO.getMessagesbyId(messageId);
    }

    public Message deletemessageId(int messageId){
        return messageDAO.deletemessageId(messageId);
    }

    public Message getMessagebyId(int messageId){
        return messageDAO.getMessagebyId(messageId);
    }

    public boolean updateMessageText(int messageId, String newMessageText){
        return messageDAO.updateMessageText(messageId, newMessageText);
    }
    
}