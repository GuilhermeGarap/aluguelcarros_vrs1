package com.aluguelcarros_vrs1.data.aluguel;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;

public record DadosEditarAluguel(

    @JsonFormat(pattern = "dd/MM/yyyy")
    @FutureOrPresent
    LocalDate dataInicio,

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Future
    LocalDate dataTermino
) {
    
    
}
