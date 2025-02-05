package com.aluguelcarros_vrs1.domain.aluguel;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroAluguel(

    @Schema(description = "Data de início do aluguel", example = "30/06/2025")
    @NotNull(message = "Indicar a data de início do aluguel é obrigatório")
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate data_inicio,

    @Schema(description = "Data de término do aluguel", example = "30/06/2025")
    @NotNull(message = "Indicar a data de término do aluguel é obrigatório")
    @Future
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate data_termino,

    @Schema(description = "Vincular um cliente ao aluguel", example = "1")
    // @NotNull(message = "É necessário vincular um cliente ao aluguel")
    Long cliente_id,

    @Schema(description = "Vincular um carro ao aluguel", example = "1")
    // @NotNull(message = "É necessário vincular um carro ao aluguel")
    Long carro_id

) {}
