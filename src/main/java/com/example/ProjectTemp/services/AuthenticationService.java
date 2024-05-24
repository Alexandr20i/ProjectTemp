package com.example.ProjectTemp.services;

import com.example.ProjectTemp.models.User;
import com.example.ProjectTemp.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(String username, String password, HttpSession session) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);

        if (user.isPresent()) {
            session.setAttribute("user", user.get());
            return true;
        }

        return false;
    }

    public void logout(HttpSession session) {
        session.removeAttribute("user");
    }
}