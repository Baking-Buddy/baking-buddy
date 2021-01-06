package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.Message;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.MessageRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
public class MessageController {
    @Autowired
    private MessageRepository messagesDao;

    @Autowired
    private UserRepository userDao;

    @GetMapping("/inbox")
    public String inbox(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userdb = userDao.getOne(user.getId());
        List<User> senderList = new ArrayList<>();
//        List<Message> userMessages = messagesDao.findMessagesByRecipient(userdb);
        List<Message> userMessages = messagesDao.findMessagesByRecipientOrSender(userdb, userdb);
        for (Message message : userMessages){
            if (message.getSender().getId() == userdb.getId()){
                continue;
            }
            if (!senderList.contains(message.getSender())){
                senderList.add(userDao.getOne(message.getSender().getId()));
            }
        }
        model.addAttribute("user", userDao.getOne(userdb.getId()));
        model.addAttribute("messages", userMessages);
        model.addAttribute("senders", senderList);
        return "inbox/inbox";
    }

    @PostMapping("/inbox/send_to_baker")
    public String sendToBaker(){
        return null;
    }
}
