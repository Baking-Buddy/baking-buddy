package com.example.bakingbuddy.demo.services;

import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userDao;

    @Autowired
    public UserService(UserRepository userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public Optional<User> findUserByEmail(String email) {
        Optional<User> opt = Optional.ofNullable(userDao.findUserByEmail(email));
        return opt;
    }

    public boolean userExists(String email) {
        return findUserByEmail(email).isPresent();
    }


}