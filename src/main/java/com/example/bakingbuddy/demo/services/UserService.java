package com.example.bakingbuddy.demo.services;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    public UserService(UserRepository userDao,
                       OrderRepository orderDao,
                       ToolRepository toolDao,
                       ConsumableRepository consumableDao,
                       ReviewRepository reviewDao) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.toolDao = toolDao;
        this.consuambleDao = consumableDao;
        this.reviewDao = reviewDao;
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
}

