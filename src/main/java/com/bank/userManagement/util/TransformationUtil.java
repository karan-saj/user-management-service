package com.bank.userManagement.util;

import com.bank.userManagement.dto.UserDTO;
import com.bank.userManagement.entity.UserEntity;
import com.bank.userManagement.exception.InvalidUserDetailsException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TransformationUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static UserDTO convertUserDetails(UserEntity userEntity) {
        if (userEntity == null) return null;

        return new UserDTO(
                userEntity.getUserId(),
                safeTrim(userEntity.getFirstName()),
                safeTrim(userEntity.getLastName()),
                userEntity.getDateOfBirth() != null ? userEntity.getDateOfBirth().toString() : null
        );
    }

    public static UserEntity convertUserDetails(UserDTO userDetails) {
        if (userDetails == null) return null;

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(safeTrim(userDetails.getFirstName()));
        userEntity.setLastName(safeTrim(userDetails.getLastName()));
        userEntity.setDateOfBirth(parseDate(userDetails.getDateOfBirth()));

        return userEntity;
    }

    private static String safeTrim(String input) {
        return input != null ? input.trim() : null;
    }

    private static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidUserDetailsException("Invalid date format. Expected yyyy-MM-dd.");
        }
    }
}
