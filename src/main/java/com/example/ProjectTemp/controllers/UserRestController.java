//package com.example.ProjectTemp.controllers;
//
//import com.example.ProjectTemp.models.User;
//import com.example.ProjectTemp.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/users")
//public class UserRestController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/{username}")
//    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
//        User user = userService.findByUsername(username);
//        if (user != null) {
//            return ResponseEntity.ok(user);
//        }
//        return ResponseEntity.notFound().build();
//    }
//}