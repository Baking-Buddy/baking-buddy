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

    @Autowired
    private UserRepository userDao;

    @Autowired
    private OrderRepository orderDao;

    @Autowired
    private ToolRepository toolDao;

    @Autowired
    private ConsumableRepository consuambleDao;

    @Autowired
    private ReviewRepository reviewDao;

    @Autowired
    private ImageRepository imageDao;



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
        Image userImage = imageDao.findByOwner(currentUser);
        if (userImage.getImageURL() == null){
            userImage.setImageURL("https://cdn.filestackcontent.com/7eJrnYVTSQIwFPNGXgnx");
            imageDao.save(userImage);
            return userImage.getImageURL();
        }
        return userImage.getImageURL();
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

    public User findUserByBakerID(long id){
        return userDao.getOne(id);
    }

}

