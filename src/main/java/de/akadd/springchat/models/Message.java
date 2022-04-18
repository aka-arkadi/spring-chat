package de.akadd.springchat.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "msgs")
public class Message {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(name="user_id")
    private int userId;
    @Column(name="user_name")
    private String userName;
    private String message;
    @Column(name="created_at")
    private LocalDateTime createdAt;

    protected Message(){

    }
    public Message(String userName, String message) {
        this.userId = 99;
        this.userName = userName;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
