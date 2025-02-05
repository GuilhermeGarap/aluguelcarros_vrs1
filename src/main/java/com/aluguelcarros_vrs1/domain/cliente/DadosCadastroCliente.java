package com.aluguelcarros_vrs1.domain.cliente;


import com.aluguelcarros_vrs1.domain.endereco.DadosEndereco;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Modelo para criar um novo cliente")
public record DadosCadastroCliente(
    @Schema(description = "Nome do Cliente", example = "Guilherme Garanhani Pereira")
    @NotBlank(message = "Nome é obrigatório")
    String nome,

    @Schema(description = "Email do Cliente", example = "guilhermegarap@gmail.com")
    @NotBlank(message = "Email é obrigatório")
    @Email
    String email,

    @Schema(description = "Telefone do Cliente", example = "(11) 99999-9999")
    @NotBlank(message = "Telefone é obrigatório")
    String telefone,

    @Schema(description = "CPF do Cliente", example = "123.456.789-00")
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp= "\\d{3}\\.?\\d{3}\\.?\\d{3}\\-?\\d{2}")
    String cpf,

    @NotNull(message = "Dados do endereço são obrigatórios")
    @Valid
    DadosEndereco endereco
) {
    
    
}
