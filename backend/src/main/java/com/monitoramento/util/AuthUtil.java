package com.monitoramento.util;

import com.monitoramento.model.user.AppRole;
import com.monitoramento.model.user.User;
import com.monitoramento.repository.auth.UserRepository;
import com.monitoramento.service.exceptions.AccessDeniedGenericException;
import com.monitoramento.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthUtil {

    @Autowired
    UserRepository userRepository;

    @Transactional(readOnly = true)
    public Long loggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ObjectNotFoundException("Usuário autenticado não encontrado"));
        return user.getUserId();
    }

    @Transactional(readOnly = true)
    public User loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ObjectNotFoundException("Usuário autenticado não encontrado"));
    }

    @Transactional(readOnly = true)
    public User userByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário de e-mail: " + email + " não encontrado"));
    }


    @Transactional(readOnly = true)
    public void validationOfContextWithUserId(Long id) {
        User usuarioLogado = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado."));

        if (!usuarioLogado.getRole().getRoleName().equals(AppRole.ROLE_ADMIN) && !usuarioLogado.getUserId().equals(id)) {
            throw new AccessDeniedGenericException("Acesso negado!");
        }
    }


}
