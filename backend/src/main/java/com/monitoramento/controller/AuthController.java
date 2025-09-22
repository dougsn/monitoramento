package com.monitoramento.controller;


import com.monitoramento.dto.user.UpdateAllUser;
import com.monitoramento.dto.user.UpdateUser;
import com.monitoramento.model.user.AppRole;
import com.monitoramento.model.user.Role;
import com.monitoramento.model.user.User;
import com.monitoramento.repository.auth.RoleRepository;
import com.monitoramento.repository.auth.UserRepository;
import com.monitoramento.security.jwt.JwtUtils;
import com.monitoramento.security.request.LoginRequest;
import com.monitoramento.security.request.SignupRequest;
import com.monitoramento.security.response.LoginResponse;
import com.monitoramento.security.response.MessageResponse;
import com.monitoramento.security.response.UserInfoResponse;
import com.monitoramento.security.services.UserDetailsImpl;
import com.monitoramento.service.exceptions.AccessDeniedGenericException;
import com.monitoramento.service.exceptions.ObjectNotFoundException;
import com.monitoramento.service.interfaces.auth.TotpService;
import com.monitoramento.service.interfaces.auth.UserService;
import com.monitoramento.util.AuthUtil;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TotpService totpService;

    @Autowired
    AuthUtil authUtil;

    @GetMapping()
    public ResponseEntity<?> findAll() {
        var user = userService.getAllUsers();
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> findById(@PathVariable Long userId) {
        var user = userService.getUserById(userId);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ObjectNotFoundException("O usuário de e-mail: " + userDetails.getUsername() + " não encontrado."));

        UserInfoResponse response = new UserInfoResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.isEnabled(),
                user.isTwoFactorEnabled(),
                user.getRole().getRoleName().name()
        );

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/update-user")
    public ResponseEntity<?> update(@RequestBody @Valid UpdateUser user) {
        try {
            return ResponseEntity.ok(userService.update(user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-all-user")
    public ResponseEntity<?> updateAllUser(@RequestBody @Valid UpdateAllUser user) {
        return ResponseEntity.ok(userService.updateAlluser(user));
    }

    @PostMapping("/public/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (signUpRequest.getRoleId() == 2 && !authUtil.loggedInUser().getRole().getRoleName().equals(AppRole.ROLE_ADMIN))
            throw new AccessDeniedGenericException("Acesso negado!");

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Usuário de e-mail: " + signUpRequest.getEmail() + " já registrado."));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        Role role = roleRepository.findById(signUpRequest.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role não encontrada."));

        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setCredentialsExpiryDate(LocalDate.now().plusYears(10));
        user.setAccountExpiryDate(LocalDate.now().plusYears(10));
        user.setTwoFactorEnabled(false);
        user.setSignUpMethod("email");

        user.setRole(role);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário registrado com sucesso!");
    }

    @PostMapping("/public/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("errorMessage", "Credenciais incorretas");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

//      set the authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromEmail(userDetails);

        // Collect roles from the UserDetails
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Prepare the response body, now including the JWT token directly in the body
        LoginResponse response = new LoginResponse(userDetails.getEmail(), roles, jwtToken, userDetails.is2faEnabled());

        // Return the response entity with the JWT token included in the response body
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username")
    public String currentUserName(@AuthenticationPrincipal UserDetails userDetails) {
        return (userDetails != null) ? userDetails.getUsername() : "";
    }

    @PostMapping("/public/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            userService.generatePasswordResetToken(email);
            return ResponseEntity.ok()
                    .body(new MessageResponse("E-mail de troca de senha enviada."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/public/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token,
                                           @RequestParam String newPassword) {
        try {
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok(new MessageResponse("Troca de senha efetuada com sucesso."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/enable-2fa")
    public ResponseEntity<String> enable2FA() {
        Long userId = authUtil.loggedInUserId();
        GoogleAuthenticatorKey secret = userService.generate2FASecret(userId);
        String qrCodeUrl = totpService.getQrCodeUrl(secret, userService.getUserById(userId).getUserName());
        return ResponseEntity.ok(qrCodeUrl);
    }

    @PostMapping("/disable-2fa")
    public ResponseEntity<String> disable2FA() {
        Long userId = authUtil.loggedInUserId();
        userService.disable2FA(userId);
        return ResponseEntity.ok("2FA desabilitado");
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<String> verify2FA(@RequestParam int code) {
        Long userId = authUtil.loggedInUserId();
        boolean isValid = userService.validate2FACode(userId, code);
        if (isValid) {
            userService.enable2FA(userId);
            return ResponseEntity.ok("2FA Verificado");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Código 2FA inválido.");
        }
    }

    @GetMapping("/user/2fa-status")
    public ResponseEntity<?> get2FAStatus() {
        User user = authUtil.loggedInUser();
        if (user != null) {
            return ResponseEntity.ok().body(Map.of("is2faEnabled", user.isTwoFactorEnabled()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
    }

    @PostMapping("/public/verify-2fa-login")
    public ResponseEntity<String> verify2FALogin(@RequestParam int code,
                                                 @RequestParam String jwtToken) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
        User user = userService.findByEmail(email).orElseThrow(() ->
                new ObjectNotFoundException("O usuario de e-mail: " + email + " não foi encontrado."));
        boolean isValid = userService.validate2FACode(user.getUserId(), code);
        if (isValid) {
            return ResponseEntity.ok("2FA Verificado.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Código 2FA inválido.");
        }
    }

}
