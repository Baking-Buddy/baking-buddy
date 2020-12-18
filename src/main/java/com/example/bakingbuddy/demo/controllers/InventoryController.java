package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.Consumable;
import com.example.bakingbuddy.demo.Model.Tool;
import com.example.bakingbuddy.demo.Model.ToolImage;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.ConsumableRepository;
import com.example.bakingbuddy.demo.Repos.ToolImageRepository;
import com.example.bakingbuddy.demo.Repos.ToolRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class InventoryController {
    @Autowired
    private ConsumableRepository consumableDao;

    @Autowired
    private ToolRepository toolDao;

    @Autowired
    private UserRepository userDao;

    @Autowired
    private ToolImageRepository toolImageDao;


    @GetMapping("/inventory/tools")
    public String userTools(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user",userDao.getOne(user.getId()));
        return "inventory/tools";
    }

    @GetMapping("/inventory/tools/add")
    public String newToolForm(Model model){
        model.addAttribute("tool", new Tool());
        return "inventory/add-tool";
    }

    @PostMapping("/inventory/tools/add")
    public String newTool(@ModelAttribute Tool toolToBeSaved, @RequestParam(name="uploadedImage") String uploadedImage){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        toolToBeSaved.setOwner(userDb);
        Tool dbTool = toolDao.save(toolToBeSaved);
        ToolImage toolImage = new ToolImage(uploadedImage, dbTool);
        toolImageDao.save(toolImage);
        return "redirect:/inventory/tools";
    }

    @GetMapping("/inventory/consumables")
    public String userConsumables(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", userDao.getOne(user.getId()));
        return "inventory/consumables";
    }

    @GetMapping("/inventory/consumables/add")
    public String newConsumbaleForm(Model model){
        model.addAttribute("consumable", new Consumable());
        return "inventory/add-consumable";
    }

    @PostMapping("/inventory/consumables/add")
    public String newConsumable(@ModelAttribute Consumable consumableToBeSaved){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        consumableToBeSaved.setOwner(userDb);
        consumableDao.save(consumableToBeSaved);
        return "redirect:/inventory/consumables";
    }

    @GetMapping("/inventory/consumables/{id}/edit")
    public String editConsumablesForm(@PathVariable long id, Model model){
        model.addAttribute("consumable", consumableDao.getOne(id));
        return "inventory/edit-consumables";
    }

    @PostMapping("inventory/consumables/{id}/edit")
    public String submitConsumableEdit(@PathVariable long id, @RequestParam(name = "amount") double newAmount){
        Consumable oldConsumable = consumableDao.getOne(id);
        oldConsumable.setAmount(newAmount);
        consumableDao.save(oldConsumable);
        return "redirect:/inventory/consumables";
    }

    @GetMapping("inventory/tools/{id}/edit")
    public String editToolsForm(@PathVariable long id, Model model){
        model.addAttribute("tool", toolDao.getOne(id));
        return "inventory/edit-tools";
    }

    @PostMapping("inventory/tools/{id}/edit")
    public String submitToolEdit(@PathVariable long id, @RequestParam(name = "description") String newDescription){
        Tool oldTool = toolDao.getOne(id);
        oldTool.setDescription(newDescription);
        toolDao.save(oldTool);
        return "redirect:/inventory/tools";
    }
}
