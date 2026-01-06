package com.hospital.appointment.service;

import com.hospital.appointment.entity.User;
import com.hospital.appointment.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repo;

    public AuthService(UserRepository repo) {
        this.repo = repo;
    }

    // LOGIN
    public boolean login(String username, String password) {
        return repo.findByUsernameAndPassword(
                username.trim(),
                password.trim()
        ) != null;
    }

    // REGISTER
    public String register(String username, String password, String confirmPassword) {

        if (!password.equals(confirmPassword)) {
            return "PASSWORD_MISMATCH";
        }

        if (repo.findByUsername(username) != null) {
            return "USER_EXISTS";
        }

        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password.trim());

        repo.save(user);
        return "SUCCESS";
    }
}
