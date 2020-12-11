package com.example.bakingbuddy.demo.Model;


import javax.persistence.*;
import java.util.List;

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
    @JoinColumn(nullable = false, name = "user_id")
    private User consumer;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User owner;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderImage> orderImages;

    public Order(){

    }

    public Order(long id, String description, double price, User consumer, User owner) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.consumer = consumer;
        this.owner = owner;
    }

    public Order(String description, double price, User consumer, User baker) {
        this.description = description;
        this.price = price;
        this.consumer = consumer;
        this.owner = owner;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
