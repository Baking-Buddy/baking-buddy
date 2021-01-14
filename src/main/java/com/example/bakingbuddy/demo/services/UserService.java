package com.example.bakingbuddy.demo.services;
import com.example.bakingbuddy.demo.Model.Image;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userDao;
    private final OrderRepository orderDao;
    private final ToolRepository toolDao;
    private final ConsumableRepository consuambleDao;
    private final ReviewRepository reviewDao;
    private final ImageRepository imageDao;

    public UserService(UserRepository userDao,
                       OrderRepository orderDao,
                       ToolRepository toolDao,
                       ConsumableRepository consumableDao,
                       ReviewRepository reviewDao,
                       ImageRepository imageDao) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.toolDao = toolDao;
        this.consuambleDao = consumableDao;
        this.reviewDao = reviewDao;
        this.imageDao = imageDao;
    }

    @Transactional
    public Optional<User> findUserByEmail(String email) {
        Optional<User> opt = Optional.ofNullable(userDao.findUserByEmail(email));
        return opt;
    }

    @Transactional
    public Optional<User> findUserByUsername(String username) {
        Optional<User> opt = Optional.ofNullable(userDao.findByUsername(username));
        return opt;
    }

    public boolean usernameExists(String username) {
        return findUserByUsername(username).isPresent();
    }

    public boolean userExists(String email) {
        return findUserByEmail(email).isPresent();
    }

    public boolean isLoggedIn(){
        boolean isAnonymousUser = SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken;
        return !isAnonymousUser;
    }

    public boolean orderOwner(User currentUser, long orderID){
        return currentUser == orderDao.getOne(orderID).getOwner();
    }

    public boolean orderBaker(User currentUser, long orderID){
        return currentUser == orderDao.getOne(orderID).getBaker();
    }

    public boolean profileOwner(User currentUser, long userID){
        return currentUser.getId() == userID;
    }

    public User sessionUser(){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDao.getOne(currentUser.getId());
    }

    public boolean toolOwner(User currentUser, long toolID){
        return currentUser == toolDao.getOne(toolID).getOwner();
    }

    public boolean consumableOwner(User currentUser, long consumableID){
        return currentUser == consuambleDao.getOne(consumableID).getOwner();
    }

    public boolean reviewOwner(User currentuser, long reviewID){
        return currentuser == reviewDao.getOne(reviewID).getOwner();
    }

    public String profileImage(User currentUser){
        try {
            Image userImage = imageDao.findByOwner(currentUser);
            if (userImage.getImageURL().isEmpty()){
                userImage.setImageURL("https://cdn.filestackcontent.com/z2yTGxRyyPn3GUz3E7wJ");
                return userImage.getImageURL();
            }
            return userImage.getImageURL();
        } catch (NullPointerException e){
            System.err.println("Looks like this user did not setup a profile picture upon registering " +
                    "causing a: " + e);
            System.err.println("UserID: " + currentUser.getId() +
                    ", Username: " + currentUser.getUsername() +
                    ", First Name: " + currentUser.getFirstName() +
                    ", Last Name: " + currentUser.getLastName() +
                    ", Email: " + currentUser.getEmail());
            System.out.println("\033[32mThe user has been assigned the default profile picture\033[0m");
            Image newUserImage = new Image();
            newUserImage.setImageURL("https://cdn.filestackcontent.com/z2yTGxRyyPn3GUz3E7wJ");
            newUserImage.setOwner(currentUser);
            newUserImage.setProfilePicture(true);
            imageDao.save(newUserImage);
            return imageDao.findByOwner(currentUser).getImageURL();
        }

    }

    public void updateResetPasswordToken(String token, String email) throws UsernameNotFoundException {
        User dbUser = userDao.findUserByEmail(email);

        if (dbUser == null) {
            throw new UsernameNotFoundException("");
        } else {
            dbUser.setResetPasswordToken(token);
            userDao.save(dbUser);
        }
    }

    public User getByResetPasswordToken(String token) {
        return userDao.findByResetPasswordToken(token);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userDao.save(user);
    }

}

