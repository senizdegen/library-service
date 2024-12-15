package kz.dos.libraryService.controllers;

import kz.dos.libraryService.dao.UserDAO;
import kz.dos.libraryService.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserDAO userDAO;

    @Autowired
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userDAO.getAll());
        return "users/all";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable("id") String id) {
        return "users/single";
    }

    @GetMapping("new")
    public String newUser(){
        return "users/new";
    }

    @PostMapping
    public String createUser(@ModelAttribute("user") User user) {
        userDAO.add(user);

        return "redirect:/users";
    }
}
