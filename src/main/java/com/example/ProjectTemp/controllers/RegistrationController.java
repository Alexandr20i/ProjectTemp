package com.example.ProjectTemp.controllers;

import com.example.ProjectTemp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/confirm-account")
    public String confirmUserAccount(@RequestParam("token") String token) {
        userService.confirmUser(token);
        return "Аккаунт успешно подтвержден!";
    }
//    confirm-account?token={token}
}
