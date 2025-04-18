package com.bank.userManagement.controller;

import com.bank.userManagement.dto.UserDTO;
import com.bank.userManagement.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController {

    final private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> fetchUserDetails(@PathVariable Long id) {
        return new ResponseEntity<>(userService.fetchUserDetails(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDetails) {
        return new ResponseEntity<>(userService.createUser(userDetails), HttpStatus.CREATED);
    }


}