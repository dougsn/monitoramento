package com.monitoramento.service.interfaces.auth;

import com.monitoramento.dto.user.UserDTO;
import com.monitoramento.dto.user.UpdateAllUser;
import com.monitoramento.dto.user.UpdateUser;
import com.monitoramento.dto.user.UpdateUserResponse;
import com.monitoramento.model.user.User;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import jakarta.mail.MessagingException;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    void updateUserRole(Long userId, String roleName);

    List<User> getAllUsers();

    UserDTO getUserById(Long id);


    UpdateUserResponse update(UpdateUser user);

    @Transactional
    UpdateUserResponse updateAlluser(UpdateAllUser user);

    void generatePasswordResetToken(String email) throws IOException, MessagingException;

    void resetPassword(String token, String newPassword);

    Optional<User> findByEmail(String email);

    void registerUser(User newUser);

    GoogleAuthenticatorKey generate2FASecret(Long userId);

    boolean validate2FACode(Long userId, int code);

    void enable2FA(Long userId);

    void disable2FA(Long userId);

}
