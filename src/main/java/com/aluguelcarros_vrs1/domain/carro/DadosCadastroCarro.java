package com.aluguelcarros_vrs1.domain.carro;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroCarro(
    
    @Schema(description = "Nome do Modelo", example = "Fusca")
    @NotBlank(message = "É necessário prover o modelo do carro para o registro") 
    String modelo, 

    @Schema(description = "Valor por dia de Aluguel", example = "100.00")
    @NotNull(message = "É necessário prover o valor por dia do aluguel do carro para o registro") 
    Float valor_dia,

    @Schema(description = "Quantidade de Carros em estoque", example = "5")
    @NotNull(message = "É necessário prover a quantidade de carros desse modelo para o registro") 
    Integer unidades
) {
    
}
