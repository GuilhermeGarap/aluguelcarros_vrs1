package com.aluguelcarros_vrs1.domain.aluguel;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public record DadosEditarAluguel(
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate data_inicio,
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate data_termino
) {
    
    
}
