package com.example.ProjectTemp.services;

import com.example.ProjectTemp.DTO.UserRegistrationDto;
import com.example.ProjectTemp.models.User;
import com.example.ProjectTemp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
        user.setPassword(registrationDto.getPassword());

        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}