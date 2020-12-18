package com.example.bakingbuddy.demo.Model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name= "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne
    private User owner;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="recipes_consumables",
            joinColumns = {@JoinColumn(name= "recipe_id")},
            inverseJoinColumns ={@JoinColumn(name= "consumable_id")}
    )
    private List<Consumable> consumables;

    public Recipe(){

    }

    public Recipe(long id, String name, User owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Consumable> getConsumables() {
        return consumables;
    }

    public void setConsumables(List<Consumable> consumables) {
        this.consumables = consumables;
    }
}
