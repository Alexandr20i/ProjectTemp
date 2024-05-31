package com.example.ProjectTemp.controllers;

import com.example.ProjectTemp.models.Post;
import com.example.ProjectTemp.services.PostService;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/{id}")
    public String getPostDetails(@PathVariable Long id, Model model, HttpSession session) {
        Post post = postService.getPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));

        model.addAttribute("post", post);

        if (session.getAttribute("user") != null) {
            model.addAttribute("username", ((User) session.getAttribute("user")).getUsername());
        }

        return "postDetails"; // Path to the Thymeleaf template for post details
    }
}
