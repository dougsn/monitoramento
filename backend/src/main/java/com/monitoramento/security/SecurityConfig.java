package com.monitoramento.security;

import com.monitoramento.config.OAuth2LoginSuccessHandler;
import com.monitoramento.model.user.AppRole;
import com.monitoramento.model.user.Role;
import com.monitoramento.model.user.User;
import com.monitoramento.repository.auth.RoleRepository;
import com.monitoramento.repository.auth.UserRepository;
import com.monitoramento.security.jwt.AuthEntryPointJwt;
import com.monitoramento.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDate;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    @Value("${password}")
    private String password;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    @Lazy
    OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;


    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    // Quando eu declaro um novo filtro de segurança, com o @Bean, ele sobrescreve o filtro padrão do Spring Security
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        // Faz com que todas as requisições necessitam de autenticação
        http.authorizeHttpRequests((request) ->
                        request
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/csrf-token").permitAll()
                                .requestMatchers("/api/auth/public/**").permitAll()
                                .requestMatchers("/oauth2/**").permitAll()
                                .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2.successHandler(oAuth2LoginSuccessHandler));
        // Dizendo ao Spring para levar a classe "unauthorizedHandler" como mecanismo padrão de exceção de autenticação.
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));
        // Adicionando o filtro de verificação do JWT antes da autenticação do usuário.
        http.addFilterBefore(authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));
            if (!userRepository.existsByEmail("dougng66@gmail.com")) {
                User admin = new User("Douglas Nascimento", "dougng66@gmail.com", passwordEncoder.encode(password));
                admin.setAccountNonLocked(true);
                admin.setAccountNonExpired(true);
                admin.setCredentialsNonExpired(true);
                admin.setEnabled(true);
                admin.setCredentialsExpiryDate(LocalDate.now().plusYears(20));
                admin.setAccountExpiryDate(LocalDate.now().plusYears(20));
                admin.setTwoFactorEnabled(false);
                admin.setSignUpMethod("email");
                admin.setRole(adminRole);
                userRepository.save(admin);
            }
        };
    }

}
