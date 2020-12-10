package com.example.bakingbuddy.demo.Model;


import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    @ManyToOne
    @JoinColumn(nullable = false, name = "consumer_id")
    private User consumer;

    @ManyToOne
    @JoinColumn(nullable = false, name = "baker_id")
    private User baker;

    public Order(){

    }

    public Order(long id, String description, double price, User consumer, User baker) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.consumer = consumer;
        this.baker = baker;
    }

    public Order(long id, String description, double price, User consumer) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.consumer = consumer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getConsumer() {
        return consumer;
    }

    public void setConsumer(User consumer) {
        this.consumer = consumer;
    }

    public User getBaker() {
        return baker;
    }

    public void setBaker(User baker) {
        this.baker = baker;
    }
}
