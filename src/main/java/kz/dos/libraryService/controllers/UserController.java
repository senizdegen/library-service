package kz.dos.libraryService.controllers;

import jakarta.validation.Valid;
import kz.dos.libraryService.exceptions.UserDeletionException;
import kz.dos.libraryService.models.User;
import kz.dos.libraryService.services.UserService;
import kz.dos.libraryService.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserService userService,UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.findAll();
        System.out.println("Users: " + users);
        model.addAttribute("users", users);
        return "users/all";
    }


    @GetMapping("/{id}")
    public String getUserById(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.findOne(id));
        model.addAttribute("books", userService.getBooksByUserId(id));

        return "users/single";
    }

    @GetMapping("/new")
    public String newUser(Model model){
        model.addAttribute("user", new User());
        return "users/new";
    }

    @PostMapping
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors()){
            return "users/new";
        }

        userService.save(user);

        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.findOne(id));

        return "users/edit";
    }

    @PatchMapping("/{id}")
    public String updateUser(@PathVariable("id") int id, @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors()){
            return "users/edit";
        }

        userService.update(id, user);

        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id, Model model) {
        try {
            userService.delete(id);
        }catch(UserDeletionException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "users/error";
        }
        userService.delete(id);
        return "redirect:/users";
    }


}
