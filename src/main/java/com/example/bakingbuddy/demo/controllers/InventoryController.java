package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.*;
import com.example.bakingbuddy.demo.Repos.*;
import com.example.bakingbuddy.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private UserService userService;

    @Autowired
    private ImageRepository imageDao;

    @GetMapping("/inventory/tools")
    public String userTools(Model model){
        if (!userService.isLoggedIn()){
            return "redirect:/login";
        }
        User sessionUser = userService.sessionUser();
        List<Tool> userTools = toolDao.findToolByOwner(sessionUser);
        List<ToolImage> userToolImages = new ArrayList<>();
        for (Tool userTool : userTools) {
            userToolImages.add(toolImageDao.findToolImageByTool(userTool));
        }
        model.addAttribute("user",sessionUser);
        model.addAttribute("userTools", userTools);
        model.addAttribute("userToolImages", userToolImages);
        model.addAttribute("isBaker", sessionUser.isBaker());
        model.addAttribute("profileImage",userService.profileImage(sessionUser));
        return "inventory/tools";
    }

    @GetMapping("/inventory/tools/add")
    public String newToolForm(Model model){
        if (!userService.isLoggedIn()){
            return "redirect:/login";
        }
        User sessionUser = userService.sessionUser();
        model.addAttribute("user", sessionUser);
        model.addAttribute("tool", new Tool());
        model.addAttribute("isBaker", sessionUser.isBaker());
        model.addAttribute("profileImage",userService.profileImage(sessionUser));
        return "inventory/add-tool";
    }

    @PostMapping("/inventory/tools/add")
    public String newTool(@ModelAttribute Tool toolToBeSaved, @RequestParam(name="uploadedImage") String uploadedImage){
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDb = userDao.getOne(sessionUser.getId());
        toolToBeSaved.setOwner(userDb);
        Tool dbTool = toolDao.save(toolToBeSaved);
        ToolImage toolImage = new ToolImage(uploadedImage, dbTool);
        toolImageDao.save(toolImage);
        return "redirect:/inventory/tools";
    }

    @GetMapping("/inventory/consumables")
    public String userConsumables(Model model){
        if (!userService.isLoggedIn()){
            return "redirect:/login";
        }
        User sessionUser = userService.sessionUser();
        model.addAttribute("user", sessionUser);
        model.addAttribute("isBaker", sessionUser.isBaker());
        model.addAttribute("profileImage",userService.profileImage(sessionUser));
        return "inventory/consumables";
    }

    @GetMapping("/inventory/consumables/add")
    public String newConsumbaleForm(Model model){
        if (!userService.isLoggedIn()){
            return "redirect:/login";
        }
        User sessionUser = userService.sessionUser();
        model.addAttribute("user", sessionUser);
        model.addAttribute("isBaker", sessionUser.isBaker());
        model.addAttribute("consumable", new Consumable());
        model.addAttribute("profileImage",userService.profileImage(sessionUser));
        return "inventory/add-consumable";
    }

    @PostMapping("/inventory/consumables/add")
    public String newConsumable(@ModelAttribute Consumable consumableToBeSaved){
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDb = userDao.getOne(sessionUser.getId());
        consumableToBeSaved.setOwner(userDb);
        consumableDao.save(consumableToBeSaved);
        return "redirect:/inventory/consumables";
    }

    @GetMapping("/inventory/consumables/{id}/edit")
    public String editConsumablesForm(@PathVariable long id, Model model){
        if (!userService.isLoggedIn()){
            return "redirect:/login";
        }
        User sessionUser = userService.sessionUser();
        if (!userService.consumableOwner(sessionUser, id)){
            return "redirect:/inventory/consumables";
        }
        Consumable consumableDb = consumableDao.getOne(id);
        model.addAttribute("user", sessionUser);
        model.addAttribute("consumable", consumableDb);
        model.addAttribute("isBaker", sessionUser.isBaker());
        model.addAttribute("profileImage",userService.profileImage(sessionUser));
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
        if (!userService.isLoggedIn()){
            return "redirect:/login";
        }
        User sessionUser = userService.sessionUser();
        if (!userService.toolOwner(sessionUser, id)){
            return "redirect:/inventory/tools";
        }
        Tool toolDb = toolDao.getOne(id);
        model.addAttribute("user", sessionUser);
        model.addAttribute("tool", toolDb);
        model.addAttribute("profileImage",userService.profileImage(sessionUser));
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
