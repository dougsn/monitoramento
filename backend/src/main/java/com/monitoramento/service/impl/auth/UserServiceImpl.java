package com.monitoramento.service.impl.auth;


import com.monitoramento.dto.user.UserDTO;
import com.monitoramento.dto.user.UpdateAllUser;
import com.monitoramento.dto.user.UpdateUser;
import com.monitoramento.dto.user.UpdateUserResponse;
import com.monitoramento.model.user.AppRole;
import com.monitoramento.model.user.PasswordResetToken;
import com.monitoramento.model.user.Role;
import com.monitoramento.model.user.User;
import com.monitoramento.repository.auth.PasswordResetTokenRepository;
import com.monitoramento.repository.auth.RoleRepository;
import com.monitoramento.repository.auth.UserRepository;
import com.monitoramento.service.exceptions.BadRequestException;
import com.monitoramento.service.exceptions.DataIntegratyViolationException;
import com.monitoramento.service.exceptions.ObjectNotFoundException;
import com.monitoramento.service.interfaces.auth.TotpService;
import com.monitoramento.service.interfaces.auth.UserService;
import com.monitoramento.util.AuthUtil;
import com.monitoramento.util.EmailService;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Value("${frontend.url}")
    String frontendUrl;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TotpService totpService;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthUtil util;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, EmailService emailService, TotpService totpService, PasswordResetTokenRepository passwordResetTokenRepository, PasswordEncoder passwordEncoder, AuthUtil util) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.totpService = totpService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.util = util;
    }

    @Override
    @Transactional
    public void updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        AppRole appRole = AppRole.valueOf(roleName);
        Role role = roleRepository.findByRoleName(appRole)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Override
    @Transactional
    public UserDTO getUserById(Long id) {
        util.validationOfContextWithUserId(id);
        User user = userRepository.findById(id).orElseThrow();
        return convertToDto(user);
    }

    private UserDTO convertToDto(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getCredentialsExpiryDate(),
                user.getAccountExpiryDate(),
                user.getTwoFactorSecret(),
                user.isTwoFactorEnabled(),
                user.getSignUpMethod(),
                user.getRole().getRoleId().longValue(),
                user.getCreatedDate(),
                user.getUpdatedDate()
        );
    }


    @Override
    @Transactional
    public UpdateUserResponse update(UpdateUser user) {
        util.validationOfContextWithUserId(user.getId());
        User userExisting = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não foi encontrado para ser atualizado."));

        userExisting.setUserName(user.getUsername());
        if (user.getPassword() != null) {
            userExisting.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(userExisting);


        return new UpdateUserResponse(userExisting.getUserId(), userExisting.getUserName());
    }

    @Override
    @Transactional
    public UpdateUserResponse updateAlluser(UpdateAllUser user) {
        util.validationOfContextWithUserId(user.getId());
        User userExisting = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não foi encontrado para ser atualizado."));

        if (!user.getEmail().equals(userExisting.getEmail())) {
            if (userRepository.existsByEmail(user.getEmail()))
                throw new BadRequestException("Usuário de e-mail: " + user.getEmail() + " já registrado.");
        }


        var role = roleRepository.findById(user.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role não encontrada."));

        userExisting.setUserName(user.getUsername());
        userExisting.setEmail(user.getEmail());
        userExisting.setRole(role);
        if (user.getPassword() != null) {
            userExisting.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(userExisting);


        return new UpdateUserResponse(userExisting.getUserId(), userExisting.getUserName());
    }


    @Override
    @Transactional
    public void generatePasswordResetToken(String email) throws IOException, MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário de e-mail: " + email + " não encontrado."));

        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);
        PasswordResetToken resetToken = new PasswordResetToken(token, expiryDate, user);
        passwordResetTokenRepository.save(resetToken);

        String resetUrl = frontendUrl + "/troca-senha?token=" + token;
        // Send email to user.
        log.info("Enviando e-mail de troca de senha.");
        emailService.sendPasswordResetEmail(user.getEmail(), resetUrl);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ObjectNotFoundException("Token de redefinição de senha inválida."));

        if (resetToken.isUsed())
            throw new DataIntegratyViolationException("O token de redefinição de senha já foi usado.");

        if (resetToken.getExpiryDate().isBefore(Instant.now()))
            throw new BadRequestException("O token de redefinição de senha expirou.");

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }

    @Override
    @Transactional
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void registerUser(User newUser) {
        if (newUser.getPassword() != null)
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
    }

    @Override
    @Transactional
    public GoogleAuthenticatorKey generate2FASecret(Long userId) {
        // Aqui é salvo o secret key do google authenticator no usuário que solicitou.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário de id: " + userId + " não foi encontrado."));
        GoogleAuthenticatorKey key = totpService.generateSecret();
        user.setTwoFactorSecret(key.getKey());
        userRepository.save(user);
        return key;
    }

    @Override
    @Transactional
    public boolean validate2FACode(Long userId, int code) {
        // Verificando o código do 2FA
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário de id: " + userId + " não foi encontrado."));
        return totpService.verifyCode(user.getTwoFactorSecret(), code);
    }

    @Override
    @Transactional
    public void enable2FA(Long userId) {
        util.validationOfContextWithUserId(userId);
        // Habilitando autenticação por 2 fatores
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário de id: " + userId + " não foi encontrado."));
        user.setTwoFactorEnabled(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void disable2FA(Long userId) {
        // Desativando autenticação por 2 fatores
        util.validationOfContextWithUserId(userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário de id: " + userId + " não foi encontrado."));
        user.setTwoFactorEnabled(false);
        userRepository.save(user);

    }


}
