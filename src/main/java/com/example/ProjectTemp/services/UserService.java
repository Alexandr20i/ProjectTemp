package com.example.ProjectTemp.services;

import com.example.ProjectTemp.DTO.UserRegistrationDto;
import com.example.ProjectTemp.models.ConfirmationToken;
//import com.example.ProjectTemp.models.Role;
import com.example.ProjectTemp.models.Group;
import com.example.ProjectTemp.models.Post;
import com.example.ProjectTemp.models.User;
import com.example.ProjectTemp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ProjectTemp.security.PasswordUtil;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PostRepository postRepository;  // Добавляем репозиторий для постов

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupSubscriptionRepository groupSubscriptionRepository;


    public User save(UserRegistrationDto registrationDto) {
        User user = new User();
        user.setName(registrationDto.getName());
        user.setFnameUser(registrationDto.getFnameUser());
        user.setBday(registrationDto.getBday());
        user.setGender(registrationDto.getGender());
        user.setCountry(registrationDto.getCountry());
        user.setCity(registrationDto.getCity());
        user.setHeight(registrationDto.getHeight());
        user.setWeight(registrationDto.getWeight());
        user.setEmail(registrationDto.getEmail());
        user.setUsername(registrationDto.getUsername());
//        user.setPassword(registrationDto.getPassword());
        user.setPassword(PasswordUtil.hashPassword(registrationDto.getPassword())); // Хэширование пароля


        User savedUser = userRepository.save(user);

        ConfirmationToken token = new ConfirmationToken(savedUser);
        confirmationTokenRepository.save(token);

        String confirmationUrl = "http://localhost:8080/confirm-account?token=" + token.getToken();
        emailService.sendSimpleMessage(savedUser.getEmail(), "Подтверждение регистрации",
                "Для подтверждения регистрации перейдите по следующей ссылке: " + confirmationUrl);

        return savedUser;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }


    public void confirmUser(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token);

        if (confirmationToken != null) {
            User user = confirmationToken.getUser();
            user.setActive(true);  // Set enabled to true or any other logic for activation
            userRepository.save(user);
        }
    }

    public void updateUserProfile(Long userId, UserRegistrationDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setName(userDto.getName());
        user.setFnameUser(userDto.getFnameUser());
        user.setBday(userDto.getBday());
        user.setGender(userDto.getGender());
        user.setCountry(userDto.getCountry());
        user.setCity(userDto.getCity());
        user.setHeight(userDto.getHeight());
        user.setWeight(userDto.getWeight());
        userRepository.save(user);
    }

    public void followUser(Long userId, Long userToFollowId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        User userToFollow = userRepository.findById(userToFollowId).orElseThrow(() -> new IllegalArgumentException("User to follow not found"));

        user.getFollowing().add(userToFollow);
        userRepository.save(user);
    }

    public Set<User> getFollowers(Long userId) {
        return userRepository.findFollowersByUserId(userId);
    }

    public Set<User> getFollowing(Long userId) {
        return userRepository.findFollowingByUserId(userId);
    }

    public void unfollowUser(Long userId, Long userToUnfollowId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        User userToUnfollow = userRepository.findById(userToUnfollowId).orElseThrow(() -> new IllegalArgumentException("User to unfollow not found"));

        user.getFollowing().remove(userToUnfollow);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(User user) {
        // Удаляем связанные посты перед удалением пользователя
        List<Post> posts = postRepository.findByUser(user);
        postRepository.deleteAll(posts);

        // Удаляем записи из таблицы users_followers и users_following
        followRepository.deleteByUser(user);
        followRepository.deleteByFollower(user);

        // Удаляем записи из таблицы groups
        List<Group> groups = groupRepository.findByOwner(user);
        groupRepository.deleteAll(groups);

        // Удаляем записи из таблицы group_subscriptions
        groupSubscriptionRepository.deleteByUser(user);

        confirmationTokenRepository.deleteByUser(user);
        userRepository.delete(user);
    }



}