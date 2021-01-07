package com.example.bakingbuddy.demo.Model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @OneToOne
    private User recipient;

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", recipient=" + recipient +
                ", sender=" + sender +
                ", body='" + body + '\'' +
                ", date=" + date +
                '}';
    }

    @OneToOne
    private User sender;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private Date date;

    public Message(){

    }

    public Message(long id, User recipient, User sender, String body){
        this.id = id;
        this.recipient = recipient;
        this.sender = sender;
        this.body = body;
    }

    public Message(User recipient, User sender, String body){
        this.recipient = recipient;
        this.sender = sender;
        this.body = body;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
