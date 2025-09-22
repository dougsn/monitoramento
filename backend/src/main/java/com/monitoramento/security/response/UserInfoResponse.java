package com.monitoramento.security.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserInfoResponse {
    private Long id;
    private String username;
    private String email;
    private boolean enabled;
    private boolean isTwoFactorEnabled;
    private String role;

    public UserInfoResponse(Long id, String username, String email, boolean enabled, boolean isTwoFactorEnabled,
                            String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.enabled = enabled;
        this.isTwoFactorEnabled = isTwoFactorEnabled;
        this.role = role;
    }
}