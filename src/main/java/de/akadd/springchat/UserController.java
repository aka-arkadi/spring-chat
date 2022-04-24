package de.akadd.springchat;

import de.akadd.springchat.models.Message;
import de.akadd.springchat.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageRepository messageRepository;

    @GetMapping("/register")
    public String registerForm() {
        String s = "<form method='POST' action='/register-user'>" +
                "<p>username : <input type='text' name='username'></p>" +
                "<p>password : <input type='text' name='password'></p>" +
                "<p>registercode : <input type='text' name='registercode'></p>" +
                "<button type='submit'>REGISTER</button>" +
                "</form>";
        return HtmlTemplate.htmlStart() + s + HtmlTemplate.htmlEnd();
    }
    @PostMapping("/register-user")
    public String registerUser(String username, String password, String registercode) {
        String s = "";
        //TODO check for forbidden names - 'BOT', 'ADMIN', already used username
        if (registercode.equals("code1234")) {
            User u = new User(username, password);
            try {
                userRepository.save(u);
                s = "<p> Registration completed. </p>";
            } catch (Exception e) {
                s = "<p> Error while saving in user repository. No registration. </p>";
            }
        } else {
            s = "<p> Register code wrong. No registration. </p>";
        }
        s = s + "<p> username: " + username + "</p>" +
                "<p> password: " + password + "</p>" +
                "<p> registercode: " + registercode + "</p>";
        return HtmlTemplate.htmlStart() + s + HtmlTemplate.htmlEnd();
    }

    @PostMapping("/delete-user/{id}")
    public String deleteUser(HttpServletRequest r, @PathVariable("id") int id) {
        String s = "";
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return "<p> <a href='/'>home</a></p><hr><p> No user not found. Id: "+id+".</p>";
        }
        User u = optionalUser.get();
        String userName = u.getUserName();
        if (r.isUserInRole("ADMIN")) {
            if (deleteUser(u)) {
                Message m = new Message("BOT", "-- user " + userName + " was deleted by admin --");
                messageRepository.save(m);
                s = "<p> User was deleted by admin. </p>";
            } else {
                s = "<p> User delete not possible. Exception of user repository.</p>";
            }
        }
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals(userName)) {
            if (deleteUser(u)) {
                Message m = new Message("BOT", "-- user " + userName + " deleted himself --");
                messageRepository.save(m);
                s = "<p> User deleted himself. </p>";
            } else {
                s = "<p> User tried to delete himself. Not working. Error exception in user repository.</p>";
            }
        }
        if (s.equals("")){
            s = "<p> You are not allowed to delete user " + userName + ".</p>";
        }
        return HtmlTemplate.htmlStart() + s + HtmlTemplate.htmlEnd();
    }
    @PostMapping("/make-admin/")
    public String makeAdmin(HttpServletRequest r, int id) {
        String s = "";
        Optional<User> optionalUser = userRepository.findById(id);
        if(!optionalUser.isPresent()){
            return "<a href='/'> HOME </a><p> User not found.</p>";
        }
        User u = optionalUser.get();
        u.setRole("ADMIN");
        userRepository.save(u);
        s = "<p> User " + u.getUserName() + " is admin now.</p>";
        return HtmlTemplate.start(r) + s + HtmlTemplate.htmlEnd();
    }

    private Boolean deleteUser(User u){
        try {
            userRepository.deleteById(u.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
