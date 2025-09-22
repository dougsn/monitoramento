package com.monitoramento.security.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class LoginResponse {
    private String jwtToken;
    private String email;
    private List<String> roles;
    private Boolean twoFactorEnabled;


    public LoginResponse(String username, List<String> roles, String jwtToken, Boolean twoFactorEnabled) {
        this.email = username;
        this.roles = roles;
        this.jwtToken = jwtToken;
        this.twoFactorEnabled = twoFactorEnabled;
    }


}