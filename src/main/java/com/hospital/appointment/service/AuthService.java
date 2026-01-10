package com.hospital.appointment.service;

import com.hospital.appointment.entity.User;
import com.hospital.appointment.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository repo) {
        this.repo = repo;
    }

    // ✅ LOGIN
    public boolean login(String username, String password) {

        Optional<User> userOpt = repo.findByUsername(username.trim());

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        return encoder.matches(password, user.getPassword());
    }

    // ✅ REGISTER
    public String register(String username, String password, String confirmPassword) {

        if (!password.equals(confirmPassword)) {
            return "PASSWORD_MISMATCH";
        }

        if (repo.findByUsername(username.trim()).isPresent()) {
            return "USER_EXISTS";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password)); // 🔐 HASHED

        repo.save(user);
        return "SUCCESS";
    }
}
