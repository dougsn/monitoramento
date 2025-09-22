package com.monitoramento.service.interfaces.auth;

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public interface TotpService {
    GoogleAuthenticatorKey generateSecret();

    String getQrCodeUrl(GoogleAuthenticatorKey secret, String username);

    boolean verifyCode(String secret, int code);
}
