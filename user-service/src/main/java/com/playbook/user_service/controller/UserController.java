package com.playbook.user_service.controller;

import com.playbook.user_service.config.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/me")
    public UserDetailsImpl getCurrentUser(@AuthenticationPrincipal UserDetailsImpl user) {
        return user;
    }
}