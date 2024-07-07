package com.julien.paymybuddy.dto;

import com.julien.paymybuddy.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Generated
public class SecuredPostUserDTO {
    private String username;
    private String email;
    private String password;

    public SecuredPostUserDTO(UserEntity userEntity) {
        this.username = userEntity.getUsername();
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
    }
}
