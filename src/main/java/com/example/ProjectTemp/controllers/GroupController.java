package com.example.ProjectTemp.controllers;

import com.example.ProjectTemp.models.Group;
import com.example.ProjectTemp.models.Post;
import com.example.ProjectTemp.models.User;
import com.example.ProjectTemp.services.GroupService;
import com.example.ProjectTemp.services.PostService;
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

    @Autowired
    private PostService postService;

    /** Метод для отображения формы создания поста в группе */
    @GetMapping("/{id}/create-post")
    public String getCreateGroupPostForm(@PathVariable Long id,
                                         HttpSession session,
                                         Model model) {
        Group group = groupService.getGroupById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID: " + id));
        model.addAttribute("group", group);
        model.addAttribute("post", new Post());

        if (session.getAttribute("user") != null) {
            model.addAttribute("username", ((User) session.getAttribute("user")).getUsername());
        }
        return "groups/create-post"; // путь к шаблону для создания поста в группе
    }

    /** Метод для обработки создания поста в группе */
    @PostMapping("/{id}/create-post")
    public String createGroupPost(@PathVariable Long id,
                                  @ModelAttribute("post") Post post,
                                  HttpSession session) {
        User user = (User) session.getAttribute("user");
        Group group = groupService.getGroupById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID: " + id));

        if (user == null) {
            return "redirect:/login";
        }

        postService.saveGroupPost(post.getContent(), user, group);
        return "redirect:/groups/" + id;
    }



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

    @PostMapping("/{id}/toggle-subscription")
    public String toggleSubscription(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Group group = groupService.getGroupById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID: " + id));

        if (groupService.isUserSubscribed(user, group)) {
            groupService.unsubscribeFromGroup(user, group);
        } else {
            groupService.subscribeToGroup(user, group);
        }

        return "redirect:/groups/" + id;
    }

    @GetMapping("/{id}")
    public String groupDetails(@PathVariable Long id,
                               HttpSession session,
                               Model model) {
        Group group = groupService.getGroupById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID: " + id));
        model.addAttribute("group", group);

        List<Post> posts = postService.findPostsByGroup(group);
        model.addAttribute("posts", posts);

        if (session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            model.addAttribute("username", user.getUsername());
            model.addAttribute("isSubscribed", groupService.isUserSubscribed(user, group));
        }
        return "groups/details"; // Path to the Thymeleaf template for group details
    }

    @GetMapping("/{id}/subscribers")
    public String groupSubscribers(@PathVariable Long id, Model model) {
        Group group = groupService.getGroupById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID: " + id));
        model.addAttribute("group", group);
        model.addAttribute("subscribers", group.getSubscribers());
        return "groups/subscribers"; // Path to the Thymeleaf template for subscribers list
    }

    @GetMapping("/{id}/edit")
    public String editGroupForm(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        Group group = groupService.getGroupById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID: " + id));
        if (user == null || !group.getOwner().getId().equals(user.getId())) {
            return "redirect:/groups/" + id;
        }
        model.addAttribute("group", group);
        return "groups/edit-group"; // Path to the Thymeleaf template for editing group
    }

    @PostMapping("/{id}/edit")
    public String updateGroup(@PathVariable Long id,
                              @RequestParam String name,
                              @RequestParam String description,
                              HttpSession session) {
        User user = (User) session.getAttribute("user");
        Group group = groupService.getGroupById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID: " + id));
        if (user == null || !group.getOwner().getId().equals(user.getId())) {
            return "redirect:/groups/" + id;
        }
        groupService.updateGroupDetails(group, name, description);
        return "redirect:/groups/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteGroup(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Group group = groupService.getGroupById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID: " + id));
        if (user == null || !group.getOwner().getId().equals(user.getId())) {
            return "redirect:/groups/" + id;
        }
        groupService.deleteGroup(group);
        return "redirect:/groups";
    }
}