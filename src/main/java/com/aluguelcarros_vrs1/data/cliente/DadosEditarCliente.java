package com.aluguelcarros_vrs1.data.cliente;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record DadosEditarCliente(
        String nome,

        @Email
        String email,

        @Pattern(
                regexp = "^\\d{10,11}$",
                message = "O telefone deve conter apenas números e ter entre 10 e 11 dígitos (com DDD)"
        )
        String telefone,

        @CPF(message = "O CPF precisa ser válido! ")
        String cpf,

        @DateTimeFormat(pattern = "dd/MM/yyyy")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,

        @Valid
        DadosEndereco endereco
) {
    
}
