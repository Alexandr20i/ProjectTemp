package com.example.ProjectTemp.services;

import com.example.ProjectTemp.DTO.UserRegistrationDto;
import com.example.ProjectTemp.models.ConfirmationToken;
//import com.example.ProjectTemp.models.Role;
import com.example.ProjectTemp.models.User;
import com.example.ProjectTemp.repository.ConfirmationTokenRepository;
import com.example.ProjectTemp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ProjectTemp.security.PasswordUtil;


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
    private EmailService emailService;

//    @Autowired
//    private RoleRepository roleRepository;


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

//        Set<Role> roles = new HashSet<>();
//        roles.add(roleRepository.findByName("ROLE_USER"));
//        user.setRoles(roles);

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

}