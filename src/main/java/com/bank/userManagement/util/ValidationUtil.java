package com.bank.userManagement.util;

import com.bank.userManagement.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class ValidationUtil {

    public boolean validateUserDetails(UserDTO userDetails) {
        if (userDetails.getFirstName().length() < 3 || userDetails.getLastName().length() < 3) {
            return false;
        }
        return true;
    }
}
