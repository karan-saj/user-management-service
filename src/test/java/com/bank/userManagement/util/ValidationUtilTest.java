package com.bank.userManagement.util;

import com.bank.userManagement.dto.UserDTO;
import com.bank.userManagement.exception.InvalidUserDetailsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class ValidationUtilTest {

    private ValidationUtil validationUtil;

    @BeforeEach
    void setUp() {
        validationUtil = new ValidationUtil();
    }

    @Test
    void shouldPassValidationForValidUser() {
        UserDTO user = new UserDTO("John", "Doe", "2000-05-20");

        assertThatCode(() -> validationUtil.validateUserDetails(user))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionForBlankNames() {
        UserDTO user = new UserDTO(" ", "Doe", "2000-01-01");

        assertThatThrownBy(() -> validationUtil.validateUserDetails(user))
                .isInstanceOf(InvalidUserDetailsException.class)
                .hasMessageContaining("cannot be blank");
    }

    @Test
    void shouldThrowExceptionForFutureDOB() {
        String futureDate = LocalDate.now().plusDays(1).toString();
        UserDTO user = new UserDTO("John", "Doe", futureDate);

        assertThatThrownBy(() -> validationUtil.validateUserDetails(user))
                .isInstanceOf(InvalidUserDetailsException.class)
                .hasMessageContaining("cannot be in the future");
    }

    @Test
    void shouldThrowExceptionForInvalidDateFormat() {
        UserDTO user = new UserDTO("John", "Doe", "20-01-2000");

        assertThatThrownBy(() -> validationUtil.validateUserDetails(user))
                .isInstanceOf(InvalidUserDetailsException.class)
                .hasMessageContaining("Invalid date of birth format");
    }

    @Test
    void shouldThrowExceptionForInvalidAge() {
        String tooYoung = LocalDate.now().minusYears(10).toString();
        UserDTO user = new UserDTO("John", "Doe", tooYoung);

        assertThatThrownBy(() -> validationUtil.validateUserDetails(user))
                .isInstanceOf(InvalidUserDetailsException.class)
                .hasMessageContaining("must be at least 18 years old");
    }
}
