package com.example.ProjectTemp.controllers;

import com.example.ProjectTemp.models.Group;
import com.example.ProjectTemp.models.User;
import com.example.ProjectTemp.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping
    public String listGroups(@RequestParam(required = false) String search,
                             HttpSession session,
                             Model model) {
        List<Group> groups;

        if (session.getAttribute("user") != null) {
            model.addAttribute("username", ((User) session.getAttribute("user")).getUsername());
        }

        if (search != null && !search.isEmpty()) {
            groups = groupService.searchGroupsByName(search);
        } else {
            groups = groupService.searchGroupsByName("");
        }
        model.addAttribute("groups", groups);
        return "groups/list"; // Path to the Thymeleaf template for listing groups
    }

    @GetMapping("/{id}")
    public String groupDetails(@PathVariable Long id,
                               HttpSession session,
                               Model model) {
        Group group = groupService.getGroupById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный Id группы:" + id));
        model.addAttribute("group", group);
        if (session.getAttribute("user") != null) {
            model.addAttribute("username", ((User) session.getAttribute("user")).getUsername());
        }
        return "groups/details"; // Path to the Thymeleaf template for group details
    }

    @GetMapping("/new")
    public String newGroupForm(HttpSession session,
                               Model model){
        if (session.getAttribute("user") != null) {
            model.addAttribute("username", ((User) session.getAttribute("user")).getUsername());
        }
        return "groups/new";
    }

    @PostMapping
    public String createGroup(@RequestParam String name,
                              HttpSession session,
                              Model model) {
        User owner = (User) session.getAttribute("user");
        if (session.getAttribute("user") != null) {
            model.addAttribute("username", ((User) session.getAttribute("user")).getUsername());
        }
        if (owner == null) {
            // Redirect to login if not logged in
            return "redirect:/login";
        }
//        System.out.println("Owner: " + owner.getName());

        groupService.createGroup(name, owner);
        return "redirect:/groups";
    }
}