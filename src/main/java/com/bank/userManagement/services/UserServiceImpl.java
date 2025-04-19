package com.bank.userManagement.services;

import com.bank.userManagement.dto.UserDTO;
import com.bank.userManagement.entity.UserEntity;
import com.bank.userManagement.exception.InvalidUserDetailsException;
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

    /**
     * Fetch user detail based on unique id shared
     * @param id unique user id
     * @return user details
     * @throws UserNotFoundException if user is not found
     * @throws RuntimeException if something went wring while trying to fetch details
     */
    @Override
    @CircuitBreaker(name = "userService", fallbackMethod = "fetchUserDetailsFallback")
    public UserDTO fetchUserDetails(Long id) {
        log.info("Fetching user details for user id : {}", id);

        try {
            return fetchStoredUserDetails(id);
        } catch (UserNotFoundException exception) {
            throw new UserNotFoundException("Error when fetching user details: "+exception.getMessage(), exception);
        } catch (Exception exception) {
            log.error("Unexpected error while fetching user details", exception);
            throw new RuntimeException("Internal error occurred while fetching user details");
        }
    }

    /**
     * Create user in db based on shared details
     * @param userDetails shared user details
     * @return user details with unique user of the user
     */
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
        } catch (InvalidUserDetailsException exception) {
            throw new InvalidUserDetailsException("Invalid user details provided for creating user: " + exception.getMessage(), exception);
        } catch (Exception ex) {
            log.error("Unexpected error during user creation", ex);
            throw new RuntimeException("Internal error occurred while creating the user");
        }
    }

    /**
     * Fetch the user details from db and convert to user readable format
     * @param id unique user id
     * @return user details
     */
    private UserDTO fetchStoredUserDetails(Long id) {
        log.debug("Fetching user details based on user id from db");
        UserEntity userDetails = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Id is not present"));

        log.info("Fetched user details successfully for user id: {}", id);
        log.debug("Converting user detail to response object");
        return convertUserDetails(userDetails);
    }

    /**
     * Convert user detail to entity object and create new user in db
     * @param userDetails shared user details
     * @return user details with unique user id
     */
    private UserDTO createNewUser(UserDTO userDetails) {
        log.debug("Converting user request to entity format");
        UserEntity userRequest = convertUserDetails(userDetails);

        log.debug("Storing new user request data in db");
        UserEntity userEntity = userRepository.save(userRequest);

        log.debug("Convert user detail to response object");
        return convertUserDetails(userEntity);
    }

    /**
     * Fallback method for fetch user if the circuit breaker is triggered
     * @param id unique user id
     * @param throwable cause for service outage
     * @throws ServiceUnavailableException user friendly message for service outage
     */
    public ResponseEntity<Object> fetchUserDetailsFallback(Long id, Throwable throwable) throws ServiceUnavailableException {
        log.error("Error fetching user details for user id {}: {}", id, throwable.getMessage());

        throw new ServiceUnavailableException("User service is temporarily unavailable. Please try again later.");
    }

    /**
     * Fallback method for create user if the circuit breaker is triggered
     * @param userDetails user details
     * @param throwable cause for service outage
     * @throws ServiceUnavailableException user friendly message for service outage
     */
    public ResponseEntity<Object> createUserFallback(UserDTO userDetails, Throwable throwable) throws ServiceUnavailableException {
        log.error("Error creating user with error : {}", throwable.getMessage());

        throw new ServiceUnavailableException("User service is temporarily unavailable. Please try again later.");
    }
}
