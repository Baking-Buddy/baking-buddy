package com.example.bakingbuddy.demo.Model;




import com.sun.mail.imap.protocol.Status;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = true)
    private double price;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date date;

    @ManyToOne
//    @JoinColumn(nullable = false, name = "user_id")
    private User baker;

    @ManyToOne
//    @JoinColumn(nullable = false, name = "user_id")
    private User owner;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderImage> orderImages;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private OrderStatus status;

    public Order(){}

    public Order(String description, User owner, OrderStatus status, Date date) {
        this.date = date;
        this.description = description;
        this.owner = owner;
        this.status = status;
    }

    public Order(long id, String description, double price, User baker, User owner) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.baker = baker;
        this.owner = owner;
    }

    public Order(String description, double price, User baker, User owner) {
        this.description = description;
        this.price = price;
        this.baker = baker;
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

    public User getBaker() {
        return baker;
    }

    public void setBaker(User baker) {
        this.baker = baker;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<OrderImage> getOrderImages() {
        return orderImages;
    }

    public void setOrderImages(List<OrderImage> orderImages) {
        this.orderImages = orderImages;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
