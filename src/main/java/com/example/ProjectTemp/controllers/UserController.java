package com.example.ProjectTemp.controllers;

import com.example.ProjectTemp.DTO.UserRegistrationDto;
import com.example.ProjectTemp.models.User;
import com.example.ProjectTemp.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
//@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/showusers")
    public String searchUserByUsername(@RequestParam("username") String username,
                                       HttpSession session,
                                       Model model) {
        User user = userService.findByUsername(username);
        if (session.getAttribute("user") != null) {
            model.addAttribute("username", ((User) session.getAttribute("user")).getUsername());
        }
        if (user != null) {
            model.addAttribute("user", user);
            return "user_profile";
        } else {
            model.addAttribute("message", "User not found!");
            return "search_user";
        }
    }




    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {
        User existing = userService.findByUsername(registrationDto.getUsername());
        if (existing != null) {
            return "redirect:/register?error";
        }

        userService.save(registrationDto);
        return "redirect:/register?success";
    }
}