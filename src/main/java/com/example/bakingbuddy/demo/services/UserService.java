package com.example.bakingbuddy.demo.services;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userDao;

    public UserService(UserRepository userDao) {
        this.userDao = userDao;
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
}

