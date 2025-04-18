package com.bank.userManagement.util;

import com.bank.userManagement.dto.UserDTO;
import com.bank.userManagement.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class TransformationUtil {
    public UserDTO convertUserDetails(UserEntity userEntity) {
        return new UserDTO(userEntity.getUserId(), userEntity.getFirstName(), userEntity.getLastName(), userEntity.getDateOfBirth());
    }

    public UserEntity convertUserDetails(UserDTO userDetails) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userDetails.getFirstName());
        userEntity.setLastName(userDetails.getLastName());
        userEntity.setDateOfBirth(userDetails.getDateOfBirth());
        return userEntity;
    }
}
