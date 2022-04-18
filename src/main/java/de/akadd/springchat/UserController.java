package de.akadd.springchat;

import de.akadd.springchat.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

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
        if (registercode.equals("code1234")) {
            User u = new User(username, password);
            try {
                userRepository.save(u);
                s = "<p> Registration completed. </p>";
            } catch (Exception e) {
                s = "<p> Register code was ok, but error while saving in user repository. No registration. </p>";
            }
        } else {
            s = "<p> Register code wrong. No registration. </p>";
        }
        s = s + "<p> username: " + username + "</p>" +
                "<p> password: " + password + "</p>" +
                "<p> registercode: " + registercode + "</p>";
        return HtmlTemplate.htmlStart() + s + HtmlTemplate.htmlEnd();
    }
}
