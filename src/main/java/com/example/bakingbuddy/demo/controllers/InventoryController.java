package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.Consumable;
import com.example.bakingbuddy.demo.Model.Tool;
import com.example.bakingbuddy.demo.Model.ToolImage;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.ConsumableRepository;
import com.example.bakingbuddy.demo.Repos.ToolImageRepository;
import com.example.bakingbuddy.demo.Repos.RecipeRepository;
import com.example.bakingbuddy.demo.Repos.ToolRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
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

    @GetMapping("/inventory/tools")
    public String userTools(Model model){
        if (!userService.isLoggedIn()){
            return "redirect:/login";
        }
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDb = userDao.getOne(sessionUser.getId());
        List<Tool> userTools = toolDao.findToolByOwner(userDb);
        List<ToolImage> userToolImages = new ArrayList<>();
        for (int i = 0; i < userTools.size(); i++){
            userToolImages.add(toolImageDao.findToolImageByTool(userTools.get(i)));
        }
        model.addAttribute("user",userDao.getOne(sessionUser.getId()));
        model.addAttribute("userTools", userTools);
        model.addAttribute("userToolImages", userToolImages);
        return "inventory/tools";
    }

    @GetMapping("/inventory/tools/add")
    public String newToolForm(Model model){
        if (!userService.isLoggedIn()){
            return "redirect:/login";
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", userDao.getOne(user.getId()));
        model.addAttribute("tool", new Tool());
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
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDb = userDao.getOne(sessionUser.getId());
        model.addAttribute("user", userDb);
        return "inventory/consumables";
    }

    @GetMapping("/inventory/consumables/add")
    public String newConsumbaleForm(Model model){
        if (!userService.isLoggedIn()){
            return "redirect:/login";
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", userDao.getOne(user.getId()));
        model.addAttribute("consumable", new Consumable());
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
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDb = userDao.getOne(sessionUser.getId());
        Consumable consumableDb = consumableDao.getOne(id);
        if (userDb != userDao.getOne(consumableDb.getOwner().getId())){
            return "redirect:/error";
        }
        model.addAttribute("user", userDb);
        model.addAttribute("consumable", consumableDb);
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
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDb = userDao.getOne(sessionUser.getId());
        Tool toolDb = toolDao.getOne(id);
        if (userDb != userDao.getOne(toolDb.getOwner().getId())){
            return "redirect:/error";
        }
        model.addAttribute("user", userDb);
        model.addAttribute("tool", toolDb);
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
