package com.bank.userManagement.services;

import com.bank.userManagement.dto.UserDTO;
import com.bank.userManagement.entity.UserEntity;
import com.bank.userManagement.exception.UserNotFoundException;
import com.bank.userManagement.repository.UserRepository;
import com.bank.userManagement.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import javax.naming.ServiceUnavailableException;

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
    @CircuitBreaker(name = "userService", fallbackMethod = "fetchUserDetailsFallback")
    public UserDTO fetchUserDetails(Long id) {
        log.info("Fetching user details for user id : {}", id);

        try {
            return fetchStoredUserDetails(id);
        } catch (UserNotFoundException exception) {
            throw exception;
        } catch (Exception exception) {
            log.error("Unexpected error while fetching user details", exception);
            throw new RuntimeException("Internal error occurred while fetching user details");
        }
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "userService", fallbackMethod = "createUserFallback")
    public UserDTO createUser(UserDTO userDetails) {
        log.info("Creating new user for user details");

        try {
            log.info("Validating user details for new user creation");
            validation.validateUserDetails(userDetails);

            log.info("Created new user successfully");
            return createNewUser(userDetails);
        } catch (Exception ex) {
            log.error("Unexpected error during user creation", ex);
            throw new RuntimeException("Internal error occurred while creating the user");
        }
    }

    private UserDTO fetchStoredUserDetails(Long id) {
        log.debug("Fetching user details based on user id from db");
        UserEntity userDetails = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Error: User Id is not present"));

        log.info("Fetched user details successfully for user id: {}", id);
        log.debug("Converting user detail to response object");
        return convertUserDetails(userDetails);
    }

    private UserDTO createNewUser(UserDTO userDetails) {
        log.debug("Converting user request to entity format");
        UserEntity userRequest = convertUserDetails(userDetails);

        log.debug("Storing new user request data in db");
        UserEntity userEntity = userRepository.save(userRequest);

        log.debug("Convert user detail to response object");
        return convertUserDetails(userEntity);
    }

    public ResponseEntity<Object> fetchUserDetailsFallback(Long id, Throwable throwable) throws ServiceUnavailableException {
        log.error("Error fetching user details for user id {}: {}", id, throwable.getMessage());

        throw new ServiceUnavailableException("User service is temporarily unavailable. Please try again later.");
    }

    public ResponseEntity<Object> createUserFallback(UserDTO userDetails, Throwable throwable) throws ServiceUnavailableException {
        log.error("Error creating user with details {}: {}", userDetails, throwable.getMessage());

        throw new ServiceUnavailableException("User service is temporarily unavailable. Please try again later.");
    }
}
