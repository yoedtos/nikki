package net.yoedtos.nikki.usecases.ports;

import lombok.Value;

@Value
public class UserData {
    private Long id;
    private String email;
    private String password;
}
