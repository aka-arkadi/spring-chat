package de.akadd.springchat;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

public class HtmlTemplate {
    @Deprecated
    public static String htmlStart() {
        String s = "<!DOCTYPE html>" +
                "<html land='en'>" +
                "<head>" +
                    "<meta charset='utf-8'>" +
                    "<meta name='viewport' content='width=device-width, initial-width=1.0'>" +
                    "<title>Spring Chat</title>" +
                    "<link rel='stylesheet' href='/style.css'>" +
                "</head>" +
                "<body>" +
                "<div class='page'>" +
                "<a href='/'>home</a>" +
                "<a href='/msgs'>msgs</a>" +
                "<a href='/login'>login</a>" +
                "<a href='/logout'>logout</a>" +
                "<a href='/user'>user</a>" +
                "<a href='/user/new-msg'>user-new-msg</a>" +
                "<a href='/admin'>admin</a>" +
                "<hr>";
        return s;
    }
    public static String htmlEnd() {
        return "</div>" +
                "</body>";
    }
    public static String start(HttpServletRequest request) {

        String s = "<!DOCTYPE html>" +
                "<html land='en'>" +
                "<head>" +
                "<meta charset='utf-8'>" +
                "<meta name='viewport' content='width=device-width, initial-width=1.0'>" +
                "<title>Quick Test</title>" +
                "<link rel='stylesheet' href='/style.css'>" +
                "</head>" +
                "<body>" +
                "<div class='page'>" +
                "<a href='/'>home</a>" +
                "<a href='/login'>login</a>" +
                "<a href='/register'>register</a>";
        if (request.isUserInRole("USER")) {
            s = s + "<a href='/logout'>logout</a>" +
                    "<a href='/msgs'>msgs</a>" +
                    "<a href='/user'>user</a>" +
                    "<a href='/user/new-msg'>new-msg</a>";
        }
        if (request.isUserInRole("ADMIN")){
            s = s + "<a href='/admin'>admin</a>";
        }
        s = s + "<hr>";
        return s;
    }
}
