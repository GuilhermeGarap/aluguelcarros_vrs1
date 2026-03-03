package com.aluguelcarros_vrs1.services;

import java.util.Arrays;
import java.util.List;

import com.aluguelcarros_vrs1.data.security.AccountCredentialsBasicDTO;
import com.aluguelcarros_vrs1.data.security.AccountCredentialsLoginDTO;
import com.aluguelcarros_vrs1.data.security.AccountCredentialsSaveDTO;
import com.aluguelcarros_vrs1.data.security.ResponseTokenDTO;
import com.aluguelcarros_vrs1.domain.Permission;
import com.aluguelcarros_vrs1.domain.User;
import com.aluguelcarros_vrs1.infra.exception.ErrorDetailsException;
import com.aluguelcarros_vrs1.repositories.PermissionRepository;
import com.aluguelcarros_vrs1.repositories.UserRepository;
import com.aluguelcarros_vrs1.security.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PermissionRepository permissionRepository;

    private final Logger logger = LoggerFactory.getLogger(AuthService.class.getName());

    public ResponseEntity<ResponseTokenDTO> sign(AccountCredentialsLoginDTO credentialsDTO) {
        String identifier = credentialsDTO.userName();

        var userOptional = repository.findByUserNameOrEmail(identifier, identifier);

        if (userOptional.isEmpty()) {
            logger.warn("Tried to signIn with nonexistent username/email : {}.", identifier);
            throw new BadCredentialsException("Credenciais de login inválidas!");
        }

        var user = userOptional.get();

        if (!passwordEncoder.matches(credentialsDTO.password(), user.getPassword())) {
            logger.warn("Tried to signIn with the wrong password of: {}.", identifier);
            throw new BadCredentialsException("Credenciais de login inválidas!");
        }

        List<String> rolesNames = user.getRoles();

        var tokenResponse = tokenProvider.createAcessToken(user.getUsername(), rolesNames);

        tokenResponse.setRoles(rolesNames);

        return ResponseEntity.ok(tokenResponse);
    }

    public ResponseEntity<ResponseTokenDTO> refreshToken(String userName, String refreshToken) {
        var user = repository.findByUserName(userName);
        if (user.isPresent()) {
            var tokenResponse = tokenProvider.refreshToken(refreshToken);
            return ResponseEntity.ok(tokenResponse);
        } else {
            logger.warn("Tried to refresh token of a nonexistent username: {}.", userName);
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
    }

    public AccountCredentialsBasicDTO save(AccountCredentialsSaveDTO dto) {
        if (repository.existsByUserName(dto.userName())) {
            throw new ErrorDetailsException("Este nome de usuário já está em uso.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Tried to create a user without being authenticated.");
            throw new AccessDeniedException("Você precisa estar autenticado para criar usuários.");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            throw new AccessDeniedException("Usuário autenticado inválido.");
        }
        User currentUser = (User) principal;

        String roleRequested = dto.role() != null ? dto.role().toUpperCase() : "MANAGER";

        validateRoleCreation(currentUser, roleRequested, dto.institutionId());

        var entity = new User(dto);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        Permission permission = permissionRepository.findByDescription(roleRequested)
                .orElseThrow(() -> new ErrorDetailsException("Role não encontrada: " + roleRequested));
        entity.setPermissions(Arrays.asList(permission));

        var saved = repository.save(entity);

        return new AccountCredentialsBasicDTO(saved.getUsername(), saved.getFullName(), saved.getRoles());
    }

    private void validateRoleCreation(User currentUser, String roleRequested, Long institutionId) {
        String currentUserRole = getCurrentUserRole(currentUser);

        if ("COMMON_USER".equals(currentUserRole)) {
            throw new AccessDeniedException("Seu perfil não tem permissão para criar usuários.");
        }

        if ("MANAGER".equals(currentUserRole)) {
            if ("ADMIN".equals(roleRequested)) {
                throw new AccessDeniedException("Apenas administradores podem criar outros perfis ADMIN.");
            }
        }

        if (institutionId != null && institutionId == 1L && !("ADMIN".equals(roleRequested))) {
            throw new AccessDeniedException("A instituição principal é reservada para administradores.");
        }
    }

    private String getCurrentUserRole(User user) {
        if (user.getPermissions() != null && !user.getPermissions().isEmpty()) {
            return user.getPermissions().get(0).getDescription();
        }
        return "UNKNOWN";
    }


    public void deleteById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ErrorDetailsException("Usuário não encontrado."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Você precisa estar autenticado.");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            throw new AccessDeniedException("Usuário autenticado inválido.");
        }
        User currentUser = (User) principal;
        String currentUserRole = getCurrentUserRole(currentUser);

        if (currentUser.getId().equals(id)) {
            throw new AccessDeniedException("Você não pode deletar sua própria conta.");
        }

        if ("MANAGER".equals(currentUserRole)) {
            String targetUserRole = getCurrentUserRole(user);
            if ("ADMIN".equals(targetUserRole)) {
                throw new AccessDeniedException("Você não pode deletar administradores.");
            }
        }

        repository.delete(user);
        logger.info("Usuário deletado: ID {} por {}", id, currentUser.getUsername());
    }
}