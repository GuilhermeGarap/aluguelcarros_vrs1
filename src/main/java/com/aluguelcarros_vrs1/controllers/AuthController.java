package com.aluguelcarros_vrs1.controllers;

import com.aluguelcarros_vrs1.controllers.docs.AuthControllerDocs;
import com.aluguelcarros_vrs1.data.security.AccountCredentialsBasicDTO;
import com.aluguelcarros_vrs1.data.security.AccountCredentialsLoginDTO;
import com.aluguelcarros_vrs1.data.security.AccountCredentialsSaveDTO;
import com.aluguelcarros_vrs1.services.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/autenticacao")
public class AuthController implements AuthControllerDocs {

    @Autowired
    AuthService service;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class.getName());

    @PostMapping("/saveUser")
    @Override
    public ResponseEntity<AccountCredentialsBasicDTO> saveUser(@Valid @RequestBody AccountCredentialsSaveDTO accountCredentialsDTO) {
        logger.info("Trying to create user: ({}).", accountCredentialsDTO.userName());
        var response = service.save(accountCredentialsDTO);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    @Override
    public ResponseEntity<?> signin(@RequestBody AccountCredentialsLoginDTO accountCredentialsDTO) {
        logger.info("Trying to signIn user: ({}).", accountCredentialsDTO.userName());
        if (credentialsIsInvalid(accountCredentialsDTO)) {
            logger.warn("Tried to signIn with null credentials.");
            throw new BadCredentialsException("Credenciais de login inválidas!");
        }
        var token = service.sign(accountCredentialsDTO);

        if (token == null) {
            logger.warn("({})'s token is null.", accountCredentialsDTO.userName());
            throw new BadCredentialsException("Credenciais de login inválidas!");
        }
        return ResponseEntity.ok().body(token);
    }

    @PutMapping("/refresh/{userName}")
    @Override
    public ResponseEntity<?> refresh(@PathVariable String userName, @RequestHeader("Authorization") String refreshToken) {
        logger.info("Trying to refresh ({})'s token.", userName);

        if (parameterAreInvalid(userName, refreshToken)) {
            logger.warn("({})'s credentials to refresh invalid.", userName);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Credenciais de login inválidas!");
        }
        var token = service.refreshToken(userName, refreshToken);

        if (token == null) {
            logger.warn("({})' token to refresh is null.", userName);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Requisição inválida");
        }

        return ResponseEntity.ok().body(token);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("Trying to delete user: ID {}", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean parameterAreInvalid(String userName, String refreshToken) {
        return StringUtils.isBlank(userName) || StringUtils.isBlank(refreshToken);
    }

    private static boolean credentialsIsInvalid(AccountCredentialsLoginDTO accountCredentialsDTO) {
        return accountCredentialsDTO == null ||
                StringUtils.isBlank(accountCredentialsDTO.password()) ||
                StringUtils.isBlank(accountCredentialsDTO.userName());
    }


}
