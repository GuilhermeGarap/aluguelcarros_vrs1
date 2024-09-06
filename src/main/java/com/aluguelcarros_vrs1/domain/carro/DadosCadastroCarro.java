package com.aluguelcarros_vrs1.domain.carro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroCarro(
    
    @NotBlank(message = "É necessário prover o modelo do carro para o registro") 
    String modelo, 

    @NotNull(message = "É necessário prover o valor por dia do aluguel do carro para o registro") 
    Float valor_dia,

    @NotNull(message = "É necessário prover a quantidade de carros desse modelo para o registro") 
    Integer unidades
) {
    
}
