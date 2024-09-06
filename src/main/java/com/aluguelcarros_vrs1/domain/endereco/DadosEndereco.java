package com.aluguelcarros_vrs1.domain.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosEndereco(

@NotBlank(message = "Prover o logradouro é obrigatório")
String logradouro,

String numero,

@NotBlank(message = "Prover o bairro é obrigatório")
String bairro,

String complemento,

@NotBlank(message = "Prover o CEP é obrigatório")
@Pattern(regexp= "\\d{5}\\-?\\d{3}")
String cep, 

@NotBlank(message = "Prover a cidade é obrigatório")
String cidade, 

@NotBlank(message = "Prover o uf é obrigatório")
String uf
) {
    
}
