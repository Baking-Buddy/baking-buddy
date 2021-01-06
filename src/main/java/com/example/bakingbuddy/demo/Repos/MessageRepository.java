package com.example.bakingbuddy.demo.Repos;

import com.example.bakingbuddy.demo.Model.Message;
import com.example.bakingbuddy.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findMessagesByRecipient(User recipient);

    List<Message> findMessagesByRecipientOrSender(User user, User sameUser);
}
