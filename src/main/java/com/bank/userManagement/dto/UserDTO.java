package com.bank.userManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    Long userId;
    @NonNull
    String firstName;
    @NonNull
    String lastName;
    @NonNull
    LocalDate dateOfBirth;
}
