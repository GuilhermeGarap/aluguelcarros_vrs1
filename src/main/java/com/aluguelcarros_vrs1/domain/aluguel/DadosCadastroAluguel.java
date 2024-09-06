package com.aluguelcarros_vrs1.domain.aluguel;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroAluguel(
    
    @NotNull(message = "Indicar a quantidade de dias que o carro será alugado é obrigatório")
    Integer dias_alugados, 

    @NotBlank(message = "Indicar a data de inicio do aluguel é obrigatório")
    @DateTimeFormat
    String data_inicio,

    @NotNull(message = "É necessário vincular um cliente ao aluguel")
    Long cliente_id,  

    @NotNull(message = "É necessário vincular um carro ao aluguel")
    Long carro_id 


) {
    
}
