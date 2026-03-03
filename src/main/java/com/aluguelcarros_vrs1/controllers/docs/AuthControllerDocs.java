package com.aluguelcarros_vrs1.controllers.docs;

import com.aluguelcarros_vrs1.data.security.AccountCredentialsBasicDTO;
import com.aluguelcarros_vrs1.data.security.AccountCredentialsLoginDTO;
import com.aluguelcarros_vrs1.data.security.AccountCredentialsSaveDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;

public interface AuthControllerDocs {
    @Operation(summary = "Cria um novo user", description = "Endpoint para enviar um objeto JSON para criar um novo usuário")
    ResponseEntity<AccountCredentialsBasicDTO> saveUser(AccountCredentialsSaveDTO data);

    @Operation(summary = "Autentica um usuário e retorna um token", description = "Endpoint para enviar dados de autenticação e receber um token de autenticação válido")
    ResponseEntity<?> signin(AccountCredentialsLoginDTO accountCredentialsDTO);

    @Operation(summary = "Renova token", description = "Endpoint para um username e renovar o token de autenticação")
    ResponseEntity<?> refresh(String userName, String refreshToken);
}
