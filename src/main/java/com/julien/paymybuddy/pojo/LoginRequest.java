package com.julien.paymybuddy.pojo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}
