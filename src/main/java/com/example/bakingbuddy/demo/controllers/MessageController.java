package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.Image;
import com.example.bakingbuddy.demo.Model.Message;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.ImageRepository;
import com.example.bakingbuddy.demo.Repos.MessageRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import com.example.bakingbuddy.demo.services.EmailService;
import com.example.bakingbuddy.demo.services.MailgunService;
import com.example.bakingbuddy.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

@Controller
public class MessageController {
    @Autowired
    private MessageRepository messagesDao;

    @Autowired
    private UserRepository userDao;

    @Autowired
    private MailgunService mailgunService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageRepository imageDao;

    @GetMapping("/inbox")
    public String inbox(Model model){
        if (userService.isLoggedIn()) {
            User sessionUser = userService.sessionUser();
            List<User> senderList = new ArrayList<>();
            List<Message> userMessages = messagesDao.findMessagesByRecipientOrSenderOrderByDateAsc(sessionUser, sessionUser);
            for (Message message : userMessages) {
                if (message.getSender().getId() == sessionUser.getId()) {
                    continue;
                }
                if (!senderList.contains(message.getSender())) {
                    senderList.add(userDao.getOne(message.getSender().getId()));
                }
            }
            model.addAttribute("isBaker", sessionUser.isBaker());
            model.addAttribute("user", userDao.getOne(sessionUser.getId()));
            model.addAttribute("messages", userMessages);
            model.addAttribute("senders", senderList);
            model.addAttribute("profileImage",userService.profileImage(sessionUser));
            return "inbox/inbox";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/inbox/{id}")
    public String sendArbitrary(@PathVariable long id, @RequestParam(name = "body") String body){
        Message messageToBeSaved = new Message();
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDb = userDao.getOne(sessionUser.getId());
        messageToBeSaved.setBody(body);
        messageToBeSaved.setRecipient(userDao.getOne(id));
        messageToBeSaved.setSender(userDb);
        messageToBeSaved.setDate(new Date());

        messagesDao.save(messageToBeSaved);

        mailgunService.sendSimpleMessage(messageToBeSaved.getRecipient(),
                "Inbox message from" + userDb.getUsername(),
                messageToBeSaved.getSender().getFirstName() + " " + messageToBeSaved.getSender().getLastName() +
                " said: " + messageToBeSaved.getBody());
        return "redirect:/inbox";
    }
}
