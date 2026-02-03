package com.aluguelcarros_vrs1.domain.cliente;


import com.aluguelcarros_vrs1.domain.endereco.DadosEndereco;

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
    @Schema(description = "Nome do Cliente", example = "Guilherme Garanhani Pereira")
    @NotBlank(message = "Nome é obrigatório")
    String nome,

    @Schema(description = "Email do Cliente", example = "guilhermegarap@gmail.com")
    @NotBlank(message = "Email é obrigatório")
    @Email
    String email,

    @Schema(description = "Telefone do Cliente", example = "11999999999")
    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(
            regexp = "^\\d{10,11}$",
            message = "O telefone deve conter apenas números e ter entre 10 e 11 dígitos (com DDD)"
    )
    String telefone,

    @Schema(description = "CPF do Cliente", example = "123.456.789-00")
    @NotBlank(message = "CPF é obrigatório")
    @CPF(message = "O CPF precisa ser válido! ")
    String cpf,

    @NotNull(message = "Data de Nascimento do Cliente é obrigatório")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dataNascimento,

    @NotNull(message = "Dados do endereço são obrigatórios")
    @Valid
    DadosEndereco endereco
) {
    
    
}
