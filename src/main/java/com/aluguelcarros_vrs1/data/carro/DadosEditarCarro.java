package com.aluguelcarros_vrs1.data.carro;


import jakarta.validation.constraints.Pattern;

public record DadosEditarCarro(
        @Pattern(
                regexp = "^\\p{L}",
                message = "Modelo só pode conter letras. Hífens e apóstrofos apenas no meio."
        )
        String modelo,
        Float valor_dia,
        Integer unidades
) {
    
    
}
