package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegister);
        app.post("/login", this::postLogin);
        app.post("/messages", this::postMessages);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessagesbyId);
        app.delete("/messages/{message_id}", this::deleteMessagesbyId);
        app.patch("/messages/{message_id}", this::updateMessages);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesbyAccountId);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    private void postRegister(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        if(account.getUsername() == null || account.getUsername().isBlank() || account.getPassword().length() < 4 || accountService.usernameExists(account.getUsername())){
            context.status(400);
        }else{
            Account newAccount = accountService.registerAccount(account);
            context.status(200).json(newAccount);
        }
    }

    private void postLogin(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account verifiedAccount = accountService.verifyLogin(account.getUsername(), account.getPassword());
        if (verifiedAccount == null) {
            context.status(401);
        }else{
            context.status(200).json(verifiedAccount);
        }
    }

    private void postMessages(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper =new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        
        if(message.getMessage_text() == null ||  message.getMessage_text().length() > 255 || message.getMessage_text().isBlank()){
            context.status(400);
            return;
        }
        if(!accountService.usernameExistsById(message.getPosted_by())){
            context.status(400);
            return;
        }
        Message addedMessage = messageService.addMessage(message);
        if (addedMessage != null){
            context.status(200).json(addedMessage);
        }else{
            context.status(400);
        }
    }

    private void getAllMessages(Context context) {
        List <Message> messages = messageService.getAllMessages();
        context.status(200).json(messages);
    }

    private void getMessagesbyId(Context context) {
        String messageIdStr = context.pathParam("message_id");
        try {
            int messageId = Integer.parseInt(messageIdStr);
            Message message = messageService.getMessagesbyId(messageId);
        if(message != null){
            context.status(200).json(message);
        }else{
            context.status(200).result("");
        }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void deleteMessagesbyId(Context context) {
        String deletemessageIdStr = context.pathParam("message_id");
        int deletemessageId = Integer.parseInt(deletemessageIdStr);
        Message messageToDelete = messageService.deletemessageId(deletemessageId);
        if(messageToDelete != null){
            context.status(200).json(messageToDelete);
        }else{
            context.status(200).result("");
        }
    }

    private void updateMessages(Context context) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message updatedMessage = mapper.readValue(context.body(), Message.class);
        String messageIdStr = context.pathParam("message_id");
        int messageId = Integer.parseInt(messageIdStr);
        String newMessageText = updatedMessage.getMessage_text(); 
        
        if(newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255){
            context.status(400);
            return;
        }

        Message existingMessage = messageService.getMessagebyId(messageId);
        if (existingMessage == null) {
            context.status(400);
            return;
        }
        existingMessage.setMessage_text(newMessageText);
        boolean updated = messageService.updateMessageText(messageId, newMessageText);
        
        if(updated){
            context.status(200).json(existingMessage);
        }else{
            context.status(400);
        }
    }

    private void getAllMessagesbyAccountId(Context context) {
        String accountIdStr = context.pathParam("account_id");
        int accountId = Integer.parseInt(accountIdStr);
        List<Message> messages = messageService.getAllMessagesbyAccountId(accountId);
        context.json(messages);
    }
    
    }