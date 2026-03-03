package com.aluguelcarros_vrs1.data.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosEndereco(

@NotBlank(message = "Prover o logradouro é obrigatório.")
@Pattern(
        regexp = "^\\p{L}+(?:[\\p{L}'-]*\\p{L})?$",
        message = "Logradouro só pode conter letras. Hífens e apóstrofos apenas no meio."
)
String logradouro,

String numero,

@NotBlank(message = "Prover o bairro é obrigatório.")
@Pattern(
        regexp = "^\\p{L}+(?:[\\p{L}'-]*\\p{L})?$",
        message = "Bairro só pode conter letras. Hífens e apóstrofos apenas no meio."
)
String bairro,

@Pattern(
        regexp = "^[\\p{L}0-9]+(?:[\\p{L}0-9 \\'-]*[\\p{L}0-9])?$",
        message = "Complemento só pode conter letras, números. Hífens e apóstrofos apenas no meio."
)
String complemento,

@NotBlank(message = "Prover o CEP é obrigatório")
@Pattern(regexp= "\\d{5}\\-?\\d{3}",
         message = "Formato do CEP deve ser XXXXX-XXX")
String cep, 

@NotBlank(message = "Prover a cidade é obrigatório")
@Pattern(
        regexp = "^\\p{L}+(?:[\\p{L}'-]*\\p{L})?$",
        message = "Cidade só pode conter letras. Hífens e apóstrofos apenas no meio."
)
String cidade, 

@NotBlank(message = "Prover o uf é obrigatório")
@Pattern(
        regexp = "^\\p{L}",
        message = "UF só pode conter letras. Hífens e apóstrofos apenas no meio."
)
String uf
) {
    
}
