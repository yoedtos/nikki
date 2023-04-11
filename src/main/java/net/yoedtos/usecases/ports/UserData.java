package net.yoedtos.usecases.ports;

import lombok.Value;

@Value
public class UserData {
    private Long id;
    private String email;
    private String password;
}
