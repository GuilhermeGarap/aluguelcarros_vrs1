package com.aluguelcarros_vrs1.data.aluguel;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroAluguel(

    @NotNull(message = "Indicar a data de início do aluguel é obrigatório.")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @FutureOrPresent
    LocalDate dataInicio,

    @NotNull(message = "Indicar a data de término do aluguel é obrigatório.")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Future
    LocalDate dataTermino,

    @NotNull(message = "É necessário vincular um cliente ao aluguel.")
    Long cliente_id,

    @NotNull(message = "É necessário vincular um carro ao aluguel.")
    Long carro_id

) {}
