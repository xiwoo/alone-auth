package com.inminhouse.alone.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inminhouse.alone.auth.config.security.UserPrincipal;
import com.inminhouse.alone.auth.exception.ResourceNotFoundException;
import com.inminhouse.alone.auth.model.User;
import com.inminhouse.alone.auth.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {

        return userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", principal.getId()));
    }
}
