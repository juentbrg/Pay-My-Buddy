package com.julien.paymybuddy.dto;

import com.julien.paymybuddy.entity.UserConnectionEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserConnectionDTO {
    private SecuredGetUserDTO user1;
    private SecuredGetUserDTO user2;

    public UserConnectionDTO(UserConnectionEntity userConnectionEntity) {
        this.user1 = new SecuredGetUserDTO(userConnectionEntity.getUser1());
        this.user2 = new SecuredGetUserDTO(userConnectionEntity.getUser2());
    }

}

