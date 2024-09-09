package com.aluguelcarros_vrs1.domain.aluguel;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroAluguel(

    @NotNull(message = "Indicar a data de início do aluguel é obrigatório")
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate data_inicio,

    @NotNull(message = "Indicar a data de término do aluguel é obrigatório")
    @Future
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate data_termino,

    // @NotNull(message = "É necessário vincular um cliente ao aluguel")
    Long cliente_id,

    // @NotNull(message = "É necessário vincular um carro ao aluguel")
    Long carro_id

) {}
