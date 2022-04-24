package de.akadd.springchat;

import de.akadd.springchat.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class MessageController {

    @Autowired
    MessageRepository messageRepository;

    @GetMapping("/msgs")
    public String allMessages(HttpServletRequest r) {
        String s = "";
        if (r.isUserInRole("ADMIN")) {
            s = "<p> You have admin role, bro. </p>";
        }
        for (Message m : messageRepository.findAll(
                PageRequest.of(0, 10,
                        Sort.by(Sort.Direction.DESC, "id")))) {
            s = s + "<p><strong>" + m.getUserName() + "</strong>: " + m.getMessage() +
                    " (" + m.getCreatedAt() + ") <a href='/msg/" + m.getId() + "'>MSG</a></p>";
        }
        return HtmlTemplate.start(r) + s + HtmlTemplate.htmlEnd();
    }
    @GetMapping("/chat/page/{page}")
    public String getMessagePage(HttpServletRequest r, @PathVariable("page") int page) {
        String s = "";
        for (Message m : messageRepository.findAll(
               PageRequest.of(page, 10,
                    Sort.by(Sort.Direction.DESC, "id")))) {
            s = s + "<p><strong>" + m.getUserName() + "</strong>: " + m.getMessage() +
                    " (" + m.getCreatedAt() + ") <a href='/msg/" + m.getId() + "'>MSG</a></p>";
        }
        s = s + "<p>";
        if (page > 0) {
            s = s +"<a href='" + (page-1) +"'>prev page</a>";
        }
        s = s + "<a href='" + (page+1) + "'>next page</a></p>";
        return HtmlTemplate.start(r) + s + HtmlTemplate.htmlEnd();
    }

    @GetMapping("/msg/{id}")
    public String oneMessage(HttpServletRequest r, @PathVariable("id") int id){
        String s = "";
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            Message m = optionalMessage.get();
            s = "<p><strong>" + m.getUserName() + "</strong>: " +
                    m.getMessage() + "(" +
                    m.getCreatedAt() + ")" +
                    "<form method='POST' action='/user/delete-msg/" + m.getId() +"'>" +
                    "<button type='submit'>Delete</button>" +
                    "</form>" +
                    "</p>";
        } else {
            s = "Message not found.";
        }
        return HtmlTemplate.htmlStart() + s + HtmlTemplate.htmlEnd();
    }

    @GetMapping("/user/new-msg")
    public String newMsgHtml(){
        return HtmlTemplate.htmlStart() +
                "<form method='POST' action='/user/save-msg'>" +
                "<input name='msg' type='text'>" +
                "<button type='submit'>Los</button>" +
                "</form>" +
                HtmlTemplate.htmlEnd();
    }

    @PostMapping("/user/save-msg")
    public String saveNewMsg(String msg) {
        if (msg.equals("")) {
            return "<p> Yeah, no null messages please.</p>";
        }
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Message m = new Message(userName, msg);
        String errorMsg = "<p> No errors. Message-Id before save in repository: "+m.getId()+" </p>";
        try {
            messageRepository.save(m);
        } catch (Exception e) {
            errorMsg = "<p> No, not working. </p>" +
                "<p> id: " + m.getId() + ", user_id: " + m.getUserName() +
                "<p> created at: " + m.getCreatedAt() + "</p>";
        }

        return HtmlTemplate.htmlStart() + "<p> Message save good? </p>" +
                "<p>" + userName+ ": " + msg + " (" + m.getCreatedAt() +")</p>"
                + errorMsg +
                HtmlTemplate.htmlEnd();
    }

    //@DeleteMapping -> Error: Method not allowed. Maybe because used as form method.
    @PostMapping("/user/delete-msg/{id}")
    public String deleteMsgByUser(@PathVariable("id") int id, HttpServletRequest r) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Message> optionalMessage = messageRepository.findById(id);
        String s = "";
        if ( optionalMessage.isPresent() ) {
            Message m = optionalMessage.get();
            if ( userName.equals(m.getUserName())) {
                try {
                    //messageRepository.deleteById(id);
                    m.setMessage("-- deleted by user --");
                    messageRepository.save(m);
                    s = "<p> Delete done by user. </p>";
                } catch (Exception e) {
                    s = "<p> Error. Delete not possible. Exception.</p>";
                }
            } else {
                s = "<p> 403. Error. User not authorized to delete this message. </p>";
            }
            if (r.isUserInRole("ADMIN")) {
                try {
                    m.setMessage("-- deleted by admin --");
                    messageRepository.save(m);
                    s = "<p> Delete by admin done.</p>";
                } catch (Exception e) {
                    s = "<p> Error. Delete by admin not ok. </p>";
                }
            }
        } else {
            s = "<p> Error. Id not found. Id: " + id + "</p>";
        }
        return HtmlTemplate.htmlStart() + s + HtmlTemplate.htmlEnd();
    }
}
