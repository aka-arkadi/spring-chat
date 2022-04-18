package de.akadd.springchat;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {

    @GetMapping("/")
    public String home() {
        return ("This is home page.");
    }

    @GetMapping("/user")
    public String user() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ("This is user page. Wellcome " + auth.getName());
    }

    @GetMapping("/user/bla")
    public String userBla() {
        return ("This is user BLA page.");
    }

    @GetMapping("/admin")
    public String admin() {
        return ("This is admin page.");
    }
}
