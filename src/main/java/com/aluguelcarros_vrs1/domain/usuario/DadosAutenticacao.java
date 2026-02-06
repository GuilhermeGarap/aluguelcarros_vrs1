package com.aluguelcarros_vrs1.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosAutenticacao(
        @NotBlank(message = "Login é obrigatório")
        @Email(message = "Email inválido")
        String login,
        @NotBlank(message = "Senha é obrigatória")
        String senha) {
    
}
