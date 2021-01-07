package com.example.bakingbuddy.demo.services;

import com.example.bakingbuddy.demo.Repos.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository usersDao;

    public UserService(UserRepository usersDao){
        this.usersDao = usersDao;
    }

    public boolean isLoggedIn(){
        boolean isAnonymousUser = SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken;
        return !isAnonymousUser;
    }
}
