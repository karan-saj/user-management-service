package com.bank.userManagement.util;

import com.bank.userManagement.dto.UserDTO;
import com.bank.userManagement.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class TransformationUtilTest {

    @Test
    void shouldConvertUserEntityToDTO() {
        UserEntity entity = new UserEntity();
        entity.setUserId(123L);
        entity.setFirstName(" John ");
        entity.setLastName(" Doe ");
        entity.setDateOfBirth(LocalDate.of(1995, 5, 15));

        UserDTO dto = TransformationUtil.convertUserDetails(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getUserId()).isEqualTo(123L);
        assertThat(dto.getFirstName()).isEqualTo("John");
        assertThat(dto.getLastName()).isEqualTo("Doe");
        assertThat(dto.getDateOfBirth()).isEqualTo("1995-05-15");
    }

    @Test
    void shouldConvertUserDTOToEntity() {
        UserDTO dto = new UserDTO();
        dto.setFirstName(" Jane ");
        dto.setLastName(" Smith ");
        dto.setDateOfBirth("1980-12-01");

        UserEntity entity = TransformationUtil.convertUserDetails(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getFirstName()).isEqualTo("Jane");
        assertThat(entity.getLastName()).isEqualTo("Smith");
        assertThat(entity.getDateOfBirth()).isEqualTo(LocalDate.of(1980, 12, 1));
    }
}
