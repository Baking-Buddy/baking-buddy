package com.example.bakingbuddy.demo.services;

import com.example.bakingbuddy.demo.Model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service("dateService")
public class DateService {

    public Date dateToStore(String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        Date convertedDate = df.parse(date);
        return convertedDate;
    }

    public String displayOrderDate(Date date){
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String dateString = df.format(date);
        return dateString;
    }



    public HashMap<Long, String> listOfOrderDates(List<Order> orders) {
        HashMap<Long, String> hashDates = new HashMap<>();
        for(Order order : orders){
            hashDates.put(order.getId(), displayOrderDate(order.getDate()));
        }
        return hashDates;
    }

}
