package com.bank.userManagement.util;

import com.bank.userManagement.dto.UserDTO;
import com.bank.userManagement.exception.InvalidUserDetailsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.bank.userManagement.common.CommonConst.MIN_USER_AGE;

@Slf4j
@Component
public class ValidationUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void validateUserDetails(UserDTO userDetails) {
        validateUserName(userDetails.getFirstName(), userDetails.getLastName());
        validateUserDateOfBirth(userDetails.getDateOfBirth());
    }

    private void validateUserName(String firstName, String lastName) {
        if (firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
            throw new InvalidUserDetailsException("First name and last name cannot be blank or only spaces.");
        }

        if (firstName.matches(".*\\d.*") || lastName.matches(".*\\d.*")) {
            throw new InvalidUserDetailsException("First name and last name cannot contain numbers.");
        }

        if (firstName.length() > 15 || lastName.length() > 15) {
            throw new InvalidUserDetailsException("First and last names should not exceed 15 characters.");
        }

        if (firstName.split(" ").length > 2) {
            throw new InvalidUserDetailsException("First name should not have more than 2 words.");
        }
    }

    private void validateUserDateOfBirth(String dateOfBirth) {
        try {
            LocalDate dob = LocalDate.parse(dateOfBirth, DATE_FORMATTER);

            if (dob.isAfter(LocalDate.now())) {
                throw new InvalidUserDetailsException("Invalid user details provided: Date of birth cannot be in the future.");
            }

            int age = Period.between(dob, LocalDate.now()).getYears();

            if (age < MIN_USER_AGE) {
                throw new InvalidUserDetailsException("Invalid user details provided: User age must be at least 18 years old.");
            }
        } catch (DateTimeParseException e) {
            throw new InvalidUserDetailsException("Invalid date of birth format. Please use yyyy-MM-dd.");
        }
    }
}
