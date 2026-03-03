package com.aluguelcarros_vrs1.data.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AccountCredentialsSaveDTO(

        @Size(min = 4, max = 30, message = "Username deve ter entre 4 e 30 caracteres")
        @NotBlank(message = "Username do usuário deve ser preenchido!")
        @Pattern(
                regexp = "^[a-zA-Z0-9._-]+$",
                message = "Username pode conter apenas letras, números, ponto, hífen e underscore"
        )
        String userName,

        @Size(min = 5, max = 120, message = "Nome completo deve ter entre 5 e 120 caracteres")
        @NotBlank(message = "Nome completo do usuário deve ser preenchido!")
        String fullName,

        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$",
                message = "Senha deve conter ao menos uma letra maiúscula, uma minúscula e um número"
        )
        @NotBlank(message = "Senha do usuário deve ser preenchido!")
        @Size(min = 8, max = 72, message = "Senha deve ter entre 8 e 72 caracteres")
        String password,

        Long institutionId,

        String role
) {
}
