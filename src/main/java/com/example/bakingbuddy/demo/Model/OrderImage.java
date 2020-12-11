package com.example.bakingbuddy.demo.Model;

import javax.persistence.*;

@Entity
@Table(name = "order_images")
public class OrderImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String imageURL;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderImage(){}

    public OrderImage(long id, String imageURL, Order order){
        this.id = id;
        this.imageURL = imageURL;
        this.order = order;
    }

    public OrderImage(String imageURL, Order order){
        this.imageURL = imageURL;
        this.order = order;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
