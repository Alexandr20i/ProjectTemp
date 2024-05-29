package com.example.ProjectTemp.controllers;

import com.example.ProjectTemp.DTO.UserRegistrationDto;
import com.example.ProjectTemp.models.Post;
import com.example.ProjectTemp.models.User;
import com.example.ProjectTemp.services.FollowService;
import com.example.ProjectTemp.services.PostService;
import com.example.ProjectTemp.services.UserService;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
//@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private FollowService followService;

    @GetMapping("/showusers")
    public String searchUserByUsername(@RequestParam("username") String username,
                                       HttpSession session,
                                       Model model) {

        User user = userService.findByUsername(username);
        User sessionUser = (User) session.getAttribute("user");

        if (session.getAttribute("user") != null) {
            model.addAttribute("username", ((User) session.getAttribute("user")).getUsername());
        }

        // нашли пользователя
        if (user != null) {

            List<User> followers = followService.getFollowers(user);
            List<User> following = followService.getFollowingUsers(user);
            List<Post> posts = postService.findPostsByUser(user); // поиск постов пользователя по id
            boolean isFollowing = sessionUser != null && followService.isFollowing(user, sessionUser);

            model.addAttribute("user", user);
            model.addAttribute("posts", posts);
            model.addAttribute("isFollowing", isFollowing);
            model.addAttribute("followersCount", followers.size());
            model.addAttribute("followingCount", following.size());


            return "user_profile";
        } else {
            model.addAttribute("message", "User not found!"); // если не находим пользователя выводим ошибку

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

    @GetMapping("/edit-profile")
    public String showEditProfileForm(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", sessionUser);
        return "edit-profile";
    }

    @PostMapping("/edit-profile")
    public String updateUserProfile(@ModelAttribute("user") UserRegistrationDto userDto, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        userService.updateUserProfile(sessionUser.getId(), userDto);
        session.setAttribute("user", userService.findById(sessionUser.getId())); // обновление данных в сессии

        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard/create-post")
    public String getUserPosts(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Пользователь не найден");
            return "error";
        }

        model.addAttribute("posts", new Post());
        return "create-post";
    }

    @PostMapping("/dashboard/create-post")
    public String createUserPost(@ModelAttribute("post") Post post,
                                 Model model,
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        post.setUser(user);
        post.setCreatedAt(new Date());
        postService.savePost(post.getContent(), user); // Передаём уже собранный объект post
        return "redirect:/dashboard";

    }

//    @GetMapping("/users/{userId}/follow")
//    public String followUser(@PathVariable Long userId, HttpSession session, ServletResponse response) {
//        User sessionUser = (User) session.getAttribute("user");
//        if (sessionUser == null) {
//            return "redirect:/login";
//        }
//
//        userService.followUser(sessionUser.getId(), userId);
//        return "redirect:/showusers?username=" + sessionUser.getUsername();  // Redirect to user profile or required page
//    }
//
//    @GetMapping("/users/{userId}/unfollow")
//    public String unfollowUser(@PathVariable Long userId, HttpSession session, ServletResponse response) {
//        User sessionUser = (User) session.getAttribute("user");
//        if (sessionUser == null) {
//            return "redirect:/login";
//        }
//
//        userService.unfollowUser(sessionUser.getId(), userId);
//        return "redirect:/showusers?username=" + sessionUser.getUsername();  // Redirect to user profile or required page
//    }
//
//    @GetMapping("/users/{userId}/followers")
//    public String showFollowers(@PathVariable Long userId, Model model, HttpSession session) {
//        Set<User> followers = userService.getFollowers(userId);
//        model.addAttribute("users", followers);
//        return "users_list";  // Display list of followers
//    }
//
//    @GetMapping("/users/{userId}/following")
//    public String showFollowing(@PathVariable Long userId, Model model, HttpSession session) {
//        Set<User> following = userService.getFollowing(userId);
//        model.addAttribute("users", following);
//        return "users_list";  // Display list of following users
//    }








    // Подписка на пользователя
    @PostMapping("/follow")
    public String followUser(@RequestParam Long userId, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        User userToFollow = userService.findById(userId);
        if (userToFollow == null || userToFollow.getId().equals(sessionUser.getId())) {
            return "redirect:/users?error";
        }

        followService.followUser(userToFollow, sessionUser);
//        return "redirect:/user_profile";
        return "redirect:/showusers?username="+userToFollow.getUsername();
    }

    // Отписка от пользователя
    @PostMapping("/unfollow")
    public String unfollowUser(@RequestParam Long userId, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        User userToUnfollow = userService.findById(userId);
        if (userToUnfollow == null) {
            return "redirect:/users?error";
        }

        followService.unfollowUser(userToUnfollow, sessionUser);
        return "redirect:/showusers?username="+userToUnfollow.getUsername();
    }

    /** Список подписчиков у пользователя*/
    @GetMapping("/followers/{userID}")
    public String showFollowers(@PathVariable Long userID, Model model){
        User user = userService.findById(userID);
        if (user == null){
            return "redirect:/";
        }
        List<User> followers = followService.getFollowers(user);
        model.addAttribute("user", followers);
        model.addAttribute("title", "Подписчики пользователя " + user.getUsername());

        return "followers";
    }

    /** Список подписок у пользователя */
    @GetMapping("/following/{userID}")
    public String showFollowing(@PathVariable Long userID, Model model){
        User user = userService.findById(userID);
        if (user == null){
            return "redirect:/";
        }
        List<User> following = followService.getFollowingUsers(user);
        model.addAttribute("user", following);
        model.addAttribute("title", "Подписки пользователя " + user.getUsername());

        return "following";
    }

    // Получить подписчиков
    @GetMapping("/followers")
    public String getFollowers(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        List<User> followers = followService.getFollowers(sessionUser);
        model.addAttribute("followers", followers);
        return "followers";
    }

    // Получить подписки
    @GetMapping("/following")
    public String getFollowing(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        List<User> following = followService.getFollowingUsers(sessionUser);
        model.addAttribute("following", following);
        return "following";
    }

}