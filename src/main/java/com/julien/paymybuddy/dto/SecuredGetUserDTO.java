package com.julien.paymybuddy.dto;

import com.julien.paymybuddy.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SecuredGetUserDTO {

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Username cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    public SecuredGetUserDTO(UserEntity userEntity) {
        this.email = userEntity.getEmail();
        this.username = userEntity.getUsername();
    }
}

