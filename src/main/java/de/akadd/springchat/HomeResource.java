package de.akadd.springchat;

import de.akadd.springchat.models.Message;
import de.akadd.springchat.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class HomeResource {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String home(HttpServletRequest r) {
        String s = HtmlTemplate.start(r) +
                "This is home page." +
                HtmlTemplate.htmlEnd();
        return s;
    }

    @GetMapping("/user")
    public String user() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> oU = userRepository.findByUserName(auth.getName());
        String s = "";
        if(oU.isPresent()){
            User u = oU.get();
            s = "<a href='/'>home</a><hr>" +
                    "<form method='POST' action='/delete-user/" + u.getId()+ "'> "+
                    "<button type='submit'>delete my account</button></form>";
        } else {
            s = "<p> Strange, strange, strange. Impossible.</p>";
        }
        return s;
    }

    @GetMapping("/user/bla")
    public String userBla() {
        return ("This is user BLA page.");
    }

    @GetMapping("/admin")
    public String admin(HttpServletRequest r) {
        String s = "";
        for (User u : userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))) {
            s = s + "<p><strong>" + u.getId() + "</strong>: " + u.getUserName() +
                    "<form method='POST' action='/delete-user/"+ u.getId() +"'>" +
                    "<button type='submit'>delete</button>" +
                    "</form>";

            if (u.getRole().equals("USER")) {
                s = s + "<form method='POST' action='/make-admin/'>" +
                        "<input type='hidden' name='id' value='" + u.getId() + "'>" +
                        "<button type='submit'>make admin</button>" +
                        "</form>";
            }
            s = s + "</p>";

        }
        return HtmlTemplate.start(r) + s + HtmlTemplate.htmlEnd();
    }
}
