package com.bank.userManagement.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    Long userId;

    @NotBlank(message = "First name for user cannot be blank or null")
    String firstName;

    @NotBlank(message = "Last name for user cannot be blank or null")
    String lastName;

    @NotNull(message = "Date of Birth for user is required")
    @Past(message = "Date of birth must be in past")
    LocalDate dateOfBirth;
}
