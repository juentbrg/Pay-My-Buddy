package com.julien.paymybuddy.dto;

import com.julien.paymybuddy.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Generated
public class SecuredGetUserDTO {
    private String username;
    private String email;

    public SecuredGetUserDTO(UserEntity userEntity) {
        this.email = userEntity.getEmail();
        this.username = userEntity.getUsername();
    }
}

