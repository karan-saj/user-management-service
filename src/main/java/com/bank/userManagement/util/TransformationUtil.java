package com.bank.userManagement.util;

import com.bank.userManagement.dto.UserDTO;
import com.bank.userManagement.entity.UserEntity;
import com.bank.userManagement.exception.InvalidUserDetailsException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TransformationUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Convert user entity to user dto
     * @param userEntity user details fetched from db
     * @return user readable details
     */
    public static UserDTO convertUserDetails(UserEntity userEntity) {
        if (userEntity == null) return null;

        return new UserDTO(
                userEntity.getUserId(),
                safeTrim(userEntity.getFirstName()),
                safeTrim(userEntity.getLastName()),
                userEntity.getDateOfBirth() != null ? userEntity.getDateOfBirth().toString() : null
        );
    }

    /**
     * Convert user details to entity object which can be stored in db
     * @param userDetails shared user details
     * @return db storable user details
     */
    public static UserEntity convertUserDetails(UserDTO userDetails) {
        if (userDetails == null) return null;

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(safeTrim(userDetails.getFirstName()));
        userEntity.setLastName(safeTrim(userDetails.getLastName()));
        userEntity.setDateOfBirth(parseDate(userDetails.getDateOfBirth()));

        return userEntity;
    }

    /**
     * Remove trailing whitespace from user first and last name
     * @param input user name
     * @return cleaned username
     */
    private static String safeTrim(String input) {
        return input != null ? input.trim() : null;
    }

    /**
     * Parse date passed by user from string format to LocalDate format
     * @param dateStr date string
     * @return LocalDate format of date
     */
    private static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidUserDetailsException("Invalid date format. Expected yyyy-MM-dd.");
        }
    }
}
