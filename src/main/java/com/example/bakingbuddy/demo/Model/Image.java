package com.example.bakingbuddy.demo.Model;

import javax.persistence.*;

@Entity
@Table(name ="images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = true)
    private boolean isProfilePicture;

    @Column(nullable = true, length = 255)
    private String imageURL;

    @Column(nullable = false)
    @ManyToOne
    @JoinColumn (name = "user_id")
    private User owner;

    public Image(){}

    public Image(boolean isProfilePicture, String imageURL, User owner) {
        this.isProfilePicture = isProfilePicture;
        this.imageURL = imageURL;
        this.owner = owner;
    }

    public Image(long id, boolean isProfilePicture, String imageURL, User owner) {
        this.id = id;
        this.isProfilePicture = isProfilePicture;
        this.imageURL = imageURL;
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isProfilePicture() {
        return isProfilePicture;
    }

    public void setProfilePicture(boolean profilePicture) {
        isProfilePicture = profilePicture;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
