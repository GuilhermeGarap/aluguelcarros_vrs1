package com.aluguelcarros_vrs1.data.cliente;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "Modelo para criar um novo cliente")
public record DadosCadastroCliente(
    @NotBlank(message = "Nome é obrigatório.")
    @Pattern(
            regexp = "^\\p{L}",
            message = "Modelo só pode conter letras. Hífens e apóstrofos apenas no meio."
    )
    String nome,

    @NotBlank(message = "Email é obrigatório.")
    @Email
    String email,

    @NotBlank(message = "Telefone é obrigatório.")
    @Pattern(
            regexp = "^\\d{10,11}$",
            message = "O telefone deve conter apenas números e ter entre 10 e 11 dígitos (com DDD)"
    )
    String telefone,

    @NotBlank(message = "CPF é obrigatório.")
    @CPF(message = "O CPF precisa ser válido! ")
    String cpf,

    @NotNull(message = "Data de Nascimento do Cliente é obrigatório.")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dataNascimento,

    @NotNull(message = "Dados do endereço são obrigatórios.")
    @Valid
    DadosEndereco endereco
) {
    
    
}
