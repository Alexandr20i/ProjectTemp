package com.example.ProjectTemp.services;

import com.example.ProjectTemp.models.User;
import com.example.ProjectTemp.repository.UserRepository;
import com.example.ProjectTemp.security.PasswordUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(String username, String password, HttpSession session) {
        User user = userRepository.findByUsername(username);

        if (user != null && PasswordUtil.checkPassword(password, user.getPassword())) {
            session.setAttribute("user", user);
            return true;
        }

        return false;
    }

    public void logout(HttpSession session) {
        session.removeAttribute("user");
    }
}