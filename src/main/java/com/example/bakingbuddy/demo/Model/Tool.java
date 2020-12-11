package com.example.bakingbuddy.demo.Model;

import javax.persistence.*;

@Entity
@Table(name = "tools")
public class Tool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "id")
    private User owner;

    public Tool(){}

    public Tool(long id, String name, String description, User owner){
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
    }

    public Tool(String name, String description, User owner){
        this.name = name;
        this.description = description;
        this.owner = owner;

    public Tool(){}

    public Tool(long id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Tool(String name, String description){
        this.name = name;
        this.description = description;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

}
