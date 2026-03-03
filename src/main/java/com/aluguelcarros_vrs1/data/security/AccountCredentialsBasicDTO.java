package com.aluguelcarros_vrs1.data.security;

public record  AccountCredentialsBasicDTO(

        String userName,
        String fullName,
        java.util.List<String> role
) {
}
