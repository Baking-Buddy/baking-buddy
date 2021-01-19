package com.example.bakingbuddy.demo.services;

import com.example.bakingbuddy.demo.Model.Message;
import com.example.bakingbuddy.demo.Model.Order;
import com.example.bakingbuddy.demo.Model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service("dateService")
public class DateService {
    //String to Date Object
    public Date dateToStore(String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = df.parse(date);
        return convertedDate;
    }
    //Date Object to String
    public String displayDate(Date date){
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String dateString = df.format(date);
        return dateString;
    }

    //Date Object to String
    public String displayDateWithTime(Date date){
        LocalDateTime dateTime =  date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        String dateTimeConvert = dateTime.format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss"));
        return dateTimeConvert;
    }

    public HashMap<Long, String> listOfMessageDates(List<Message> messages){
        HashMap<Long, String> hashDateTime = new HashMap<>();
        for (Message message : messages){
            hashDateTime.put(message.getId(), displayDateWithTime(message.getDate()));
        }
        return hashDateTime;
    }





    public HashMap<Long, String> listOfOrderDates(List<Order> orders) {
        HashMap<Long, String> hashDates = new HashMap<>();
        for(Order order : orders){
            hashDates.put(order.getId(), displayDate(order.getDate()));
        }
        return hashDates;
    }

    public HashMap<Long, String> listOfReviewDates(List<Review> reviews) {
        HashMap<Long, String> hashDates = new HashMap<>();
        for(Review review : reviews){
            hashDates.put(review.getId(), displayDate(review.getDate()));
        }
        return hashDates;
    }

}
