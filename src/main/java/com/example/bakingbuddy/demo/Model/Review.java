package com.example.bakingbuddy.demo.Model;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 1)
    private int rating;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 255)
    private String body;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private Date date;

    @ManyToOne
    private User owner;

    @ManyToOne
    private User baker;

    public Review(){}

    public Review(long id, int rating, String title, String body, Date date) {
        this.id = id;
        this.rating = rating;
        this.title = title;
        this.body = body;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getBaker() {
        return baker;
    }

    public void setBaker(User baker) {
        this.baker = baker;
    }
}
