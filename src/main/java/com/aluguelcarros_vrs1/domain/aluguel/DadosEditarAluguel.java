package com.aluguelcarros_vrs1.domain.aluguel;

import java.time.LocalDate;

import jakarta.validation.constraints.Pattern;

public record DadosEditarAluguel(
    @Pattern(regexp= "\\d{2}\\-?\\d{2}\\-?\\d{4}")
    LocalDate data_inicio,
    @Pattern(regexp= "\\d{2}\\-?\\d{2}\\-?\\d{4}")
    LocalDate data_termino
) {
    
    
}
