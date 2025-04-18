package com.bank.userManagement.services;

import com.bank.userManagement.dto.UserDTO;

public interface UserService {

    public UserDTO fetchUserDetails(Long id);

    public UserDTO createUser(UserDTO userDTO);
}
