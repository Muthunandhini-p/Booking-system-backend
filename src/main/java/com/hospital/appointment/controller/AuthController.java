package com.hospital.appointment.controller;
import com.hospital.appointment.dto.RegisterRequest;
import com.hospital.appointment.entity.User;
import com.hospital.appointment.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public boolean login(@RequestBody User user) {
        return service.login(user.getUsername(), user.getPassword());
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest req) {
        return service.register(
                req.getUsername(),
                req.getPassword(),
                req.getConfirmPassword()
        );
    }
}
