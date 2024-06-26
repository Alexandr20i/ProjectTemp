package com.example.ProjectTemp.controllers;

import com.example.ProjectTemp.services.AuthenticationService;
import com.example.ProjectTemp.models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/")
    public String root(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("username", ((User) session.getAttribute("user")).getUsername());
            return "index";
        }
        return "index";
    }

    @GetMapping("/searchuser")
    public String showSearchUserForm(HttpSession session, Model model){
        if (session.getAttribute("user") != null) {
            model.addAttribute("username", ((User) session.getAttribute("user")).getUsername());
            return "search_user";
        }
        return "search_user";
    }


    @GetMapping("/login")
    public String login(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("username", ((User) session.getAttribute("user")).getUsername());
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginUserAccount(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        if (authenticationService.authenticate(username, password, session)) {
            model.addAttribute("username");
            return "redirect:/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Неверное имя пользователя или пароль");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        authenticationService.logout(session);
        return "redirect:/";
    }

    @GetMapping("/dashboard")
    public String userIndex(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("username", ((User) session.getAttribute("user")).getUsername());

        return "dashboard";
    }
}