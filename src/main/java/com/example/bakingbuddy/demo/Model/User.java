package com.example.bakingbuddy.demo.Model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;


@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message="Enter your first name")
    private String firstName;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "enter your last name")
    private String lastName;

    @Column(nullable = true, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Enter your email")
    @Email(message = "enter a valid email Address")
    private String email;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Enter your password")
    @Length(min = 6, message = "Password Must be at least 6 Characters")
    private String password;

    @NotBlank(message = "re-enter your password")
    private String rpassword;

    @Column(nullable = false)
    private boolean isBaker;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Enter your city")
    private String city;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "enter your state")
    private String state;

    @OneToMany(mappedBy = "owner")
    private List<Image> images;

    @OneToMany(mappedBy = "owner")
    private List<Tool> tools;

    @OneToMany(mappedBy = "owner")
    private List<Consumable> consumables;

    @OneToMany(mappedBy = "owner")
    private List<Order> orders;

    @OneToMany(mappedBy = "owner")
    private List<Recipe> recipes;

    @OneToMany(mappedBy = "owner")
    private List<Review> reviews;

    public User(){}

    public User(User copy) {
        id = copy.id;
        email = copy.email;
        username = copy.username;
        password = copy.password;
    }

    public User(String firstName, String lastName, String username, String email, String password, String rpassword, boolean isBaker, String city, String state, List<Consumable> consumables) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.rpassword = rpassword;
        this.isBaker = isBaker;
        this.city = city;
        this.state = state;
        this.consumables = consumables;
    }

    public User(long id, String firstName, String lastName, String username, String email, String password, String rpassword, boolean isBaker, String city, String state, List<Consumable> consumables) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.rpassword = rpassword;
        this.isBaker = isBaker;
        this.city = city;
        this.state = state;
        this.consumables = consumables;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBaker() {
        return isBaker;
    }

    public void setBaker(boolean baker) {
        this.isBaker = baker;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Tool> getTools() {
        return tools;
    }

    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }

    public List<Consumable> getConsumables() {
        return consumables;
    }

    public void setConsumables(List<Consumable> consumables) {
        this.consumables = consumables;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getRpassword() {
        return rpassword;
    }

    public void setRpassword(String rpassword) {
        this.rpassword = rpassword;
    }
}

