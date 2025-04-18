package com.bank.userManagement.services;

import com.bank.userManagement.dto.UserDTO;
import com.bank.userManagement.entity.UserEntity;
import com.bank.userManagement.excpetion.UserNotFoundException;
import com.bank.userManagement.repository.UserRepository;
import com.bank.userManagement.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.bank.userManagement.util.TransformationUtil.convertUserDetails;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    final private UserRepository userRepository;
    final private ValidationUtil validation;

    public UserServiceImpl(UserRepository userRepository,
                           ValidationUtil validation) {
        this.userRepository = userRepository;
        this.validation = validation;
    }

    @Override
    public UserDTO fetchUserDetails(Long id) {
        log.info("Fetching user details for user id : {}", id);

        return fetchStoredUserDetails(id);
    }

    @Override
    public UserDTO createUser(UserDTO userDetails) {
        log.info("Creating new user for user details");

        log.info("Validating user details for new user creation");
        validation.validateUserDetails(userDetails);

        log.info("Created new user successfully");
        return createNewUser(userDetails);
    }

    private UserDTO fetchStoredUserDetails(Long id) {
        log.trace("Fetching user details based on user id from db");
        UserEntity userDetails = userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("Error: User Id is not present"));

        log.info("Fetched user details successfully for user id: {}", id);
        log.trace("Converting user detail to response object");
        return convertUserDetails(userDetails);
    }

    private UserDTO createNewUser(UserDTO userDetails) {
        log.trace("Converting user request to entity format");
        UserEntity userRequest = convertUserDetails(userDetails);

        log.trace("Storing new user request data in db");
        UserEntity userEntity = userRepository.save(userRequest);

        log.trace("Convert user detail to response object");
        return convertUserDetails(userEntity);
    }
}
