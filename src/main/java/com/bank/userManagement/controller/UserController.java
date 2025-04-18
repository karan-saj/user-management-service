package com.bank.userManagement.controller;

import com.bank.userManagement.dto.UserDTO;
import com.bank.userManagement.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/user")
@Tag(name = "User Controller", description = "Handle user data related operations")
public class UserController {

    final private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch user details by ID", description = "Fetches user details for the given user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details fetched successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> fetchUserDetails(@PathVariable Long id) {
        return new ResponseEntity<>(userService.fetchUserDetails(id), HttpStatus.OK);
    }

    @PostMapping()
    @Operation(summary = "Create a new user", description = "Creates a new user and stores the details in db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid user details")
    })
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDetails) {
        return new ResponseEntity<>(userService.createUser(userDetails), HttpStatus.CREATED);
    }


}