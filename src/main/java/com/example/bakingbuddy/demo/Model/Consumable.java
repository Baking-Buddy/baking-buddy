package com.example.bakingbuddy.demo.Model;

import javax.annotation.Generated;
import
import javax.persistence.*;

@Entity
@Table(name = "consumables")
public class Consumable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 45)
    private String type;

    @Column(nullable = false, length = 45)
    private String brand;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String weightUnit;

    @ManyToOne
    @JoinColumn(name = "id")
    private User owner;


    public Consumable() {}

    public Consumable(long id, String type, String brand, double amount, String weightUnit, User owner) {
        this.id = id;
        this.type = type;
        this.brand = brand;
        this.amount = amount;
        this.weightUnit = weightUnit;
        this.owner = owner;
    }

    public Consumable(String type, String brand, double amount, String weightUnit, User owner) {
        this.type = type;
        this.brand = brand;
        this.amount = amount;
        this.weightUnit = weightUnit;
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
