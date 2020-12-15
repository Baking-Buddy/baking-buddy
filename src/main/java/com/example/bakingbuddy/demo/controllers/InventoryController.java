package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.Consumable;
import com.example.bakingbuddy.demo.Model.Tool;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.ConsumableRepository;
import com.example.bakingbuddy.demo.Repos.ToolRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class InventoryController {
    @Autowired
    private ConsumableRepository consumableDao;

    @Autowired
    private ToolRepository toolDao;

    @Autowired
    private UserRepository userDao;

    @GetMapping("/inventory/tools")
    public String userTools(Model model){
        User user = userDao.getOne(1L);
        model.addAttribute("user",user);
        return "inventory/tools";
    }

    @GetMapping("/inventory/tools/add")
    public String newToolForm(Model model){
        model.addAttribute("tool", new Tool());
        return "inventory/add-tool";
    }

    @PostMapping("/inventory/tools/add")
    public String newTool(@ModelAttribute Tool toolToBeSaved){
        User userDb = userDao.getOne(1L);
        toolToBeSaved.setOwner(userDb);
        toolDao.save(toolToBeSaved);
        return "redirect:/inventory/tools";
    }

    @GetMapping("/inventory/consumables")
    public String userConsumables(Model model){
        User user = userDao.getOne(1L);
        model.addAttribute("user", user);
        return "inventory/consumables";
    }

    @GetMapping("/inventory/consumables/add")
    public String newConsumbaleForm(Model model){
        model.addAttribute("consumable", new Consumable());
        return "inventory/add-consumable";
    }

    @PostMapping("/inventory/consumables/add")
    public String newConsumable(@ModelAttribute Consumable consumableToBeSaved){
        User userDb = userDao.getOne(1L);
        consumableToBeSaved.setOwner(userDb);
        consumableDao.save(consumableToBeSaved);
        return "redirect:/inventory/consumables";
    }
}
