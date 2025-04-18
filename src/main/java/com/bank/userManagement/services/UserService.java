package com.bank.userManagement.services;

import com.bank.userManagement.dto.UserDTO;
import com.bank.userManagement.entity.UserEntity;
import com.bank.userManagement.repository.UserRepository;
import com.bank.userManagement.util.TransformationUtil;
import com.bank.userManagement.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;

@Service
public class UserService {

    final private UserRepository userRepository;
    final private TransformationUtil util;
    final private ValidationUtil validation;

    public UserService(UserRepository userRepository,
                       TransformationUtil util,
                       ValidationUtil validation) {
        this.userRepository = userRepository;
        this.util = util;
        this.validation = validation;
    }

    public UserDTO fetchUserDetails(Long id) {
        try {
            UserEntity userDetails = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Id is not Present"));
            return util.convertUserDetails(userDetails);
        } catch (Error error) {
            throw new RuntimeException("Error while trying to fetch user details: ", error);
        }
    }

    public UserDTO createUser(UserDTO userDetails) {
        try {
            if (!validation.validateUserDetails(userDetails)) {
                throw new InputMismatchException("Error user details passed are not valid");
            }
            UserEntity userEntity = userRepository.save(util.convertUserDetails(userDetails));
            return util.convertUserDetails(userEntity);
        } catch (Error error) {
            throw new RuntimeException("Error while trying to create user: ", error);
        }
    }
}
