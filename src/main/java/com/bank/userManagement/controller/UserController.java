package com.bank.userManagement.controller;

import com.bank.userManagement.dto.UserDTO;
import com.bank.userManagement.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    final private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserDTO fetchUserDetails(@PathVariable Long id) {
        return userService.fetchUserDetails(id);
    }

    @PostMapping()
    public UserDTO createUser(@RequestBody UserDTO userDetails) {
        return userService.createUser(userDetails);
    }


}