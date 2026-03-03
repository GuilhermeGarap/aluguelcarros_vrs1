package com.aluguelcarros_vrs1.data.carro;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroCarro(

    @NotBlank(message = "É necessário prover o modelo do carro para o registro.")
    @Pattern(
            regexp = "^\\p{L}",
            message = "Modelo só pode conter letras. Hífens e apóstrofos apenas no meio."
    )
    String modelo, 

    @NotNull(message = "É necessário prover o valor por dia do aluguel do carro para o registro.")
    Float valor_dia,

    @NotNull(message = "É necessário prover a quantidade de carros desse modelo para o registro.")
    Integer unidades
) {
    
}
