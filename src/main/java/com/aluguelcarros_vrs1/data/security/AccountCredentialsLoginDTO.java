package com.aluguelcarros_vrs1.data.security;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.Pattern;

public record AccountCredentialsLoginDTO(
        @Pattern(
                regexp = "^[a-zA-Z0-9._-]+$",
                message = "Username só contém letras, números, ponto, hífen e underscore"
        )
        @JsonAlias({"username", "email"})
        String userName,
        String fullName,
        String password
) {

}

