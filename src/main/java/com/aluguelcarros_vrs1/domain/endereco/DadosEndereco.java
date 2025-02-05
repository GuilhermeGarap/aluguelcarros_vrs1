package com.aluguelcarros_vrs1.domain.endereco;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosEndereco(

@Schema(description = "Logradouro do endereço", example = "Rua dos Girassóis")
@NotBlank(message = "Prover o logradouro é obrigatório")
String logradouro,

@Schema(description = "Número do endereço", example = "123")
String numero,

@Schema(description = "Bairro do endereço", example = "Jardim das Flores")
@NotBlank(message = "Prover o bairro é obrigatório")
String bairro,

@Schema(description = "Complemento do endereço", example = "Casa 1")
String complemento,

@Schema(description = "CEP do endereço", example = "12345-678")
@NotBlank(message = "Prover o CEP é obrigatório")
@Pattern(regexp= "\\d{5}\\-?\\d{3}")
String cep, 

@Schema(description = "Cidade do endereço", example = "São Paulo")
@NotBlank(message = "Prover a cidade é obrigatório")
String cidade, 

@Schema(description = "UF do endereço", example = "SP")
@NotBlank(message = "Prover o uf é obrigatório")
String uf
) {
    
}
