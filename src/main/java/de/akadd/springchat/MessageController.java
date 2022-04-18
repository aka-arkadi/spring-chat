package de.akadd.springchat;

import de.akadd.springchat.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class MessageController {

    @Autowired
    MessageRepository messageRepository;

    @GetMapping("/msgs")
    public String allMessages() {
        String s = "";
        for (Message m : messageRepository.findAll()) {
            s = s + "<p>" + m.getUserName() + ": " + m.getMessage() +
                    " (" + m.getCreatedAt() + ") </p>";
        }
        return s;
    }

    @GetMapping("/msg/{id}")
    public String oneMessage(@PathVariable("id") int id){
        String s = "";
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            Message m = optionalMessage.get();
            s = "<p><strong>" + m.getUserName() + "</strong>: " +
                    m.getMessage() + "(" +
                    m.getCreatedAt() + ")</p>";
        } else {
            s = "Message not found.";
        }
        return s;
    }

    @GetMapping("/user/new-msg")
    public String newMsgHtml(){
        return "<form method='POST' action='/user/save-msg'>" +
                "<input name='msg' type='text'>" +
                "<button type='submit'>Los</button>" +
                "</form>";
    }

    @PostMapping("/user/save-msg")
    public String saveNewMsg(String msg) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        String errorMsg = "";
        Message m = new Message(userName, msg);
        try {
            messageRepository.save(m);
        } catch (Exception e) {
            errorMsg = "<p> No, not working. </p>" +
                "<p> id: " + m.getId() + ", user_id: " + m.getUserId() +
                "<p> created at: " + m.getCreatedAt() + "</p>";
        }

        return "<p> Message save good? </p>" +
                "<p>" + userName+ ": " + msg + " (" + m.getCreatedAt() +")</p>"
                + errorMsg;
    }
}
