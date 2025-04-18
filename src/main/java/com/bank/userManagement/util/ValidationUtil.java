package com.bank.userManagement.util;

import com.bank.userManagement.dto.UserDTO;
import com.bank.userManagement.exception.InvalidUserDetailsException;
import org.springframework.stereotype.Component;

@Component
public class ValidationUtil {

    public void validateUserDetails(UserDTO userDetails) {
        if (userDetails.getFirstName().length() < 3 || userDetails.getLastName().length() < 3) {
            throw new InvalidUserDetailsException("Invalid user details provided: First name or last name is too short.");
        }
    }
}
