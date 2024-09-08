package com.aluguelcarros_vrs1.domain.aluguel;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroAluguel(

    @NotNull(message = "Indicar a data de início do aluguel é obrigatório")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Pattern(regexp= "\\d{2}\\-?\\d{2}\\-?\\d{4}")
    LocalDate data_inicio,

    @NotNull(message = "Indicar a data de término do aluguel é obrigatório")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Pattern(regexp= "\\d{2}\\-?\\d{2}\\-?\\d{4}")
    LocalDate data_termino,

    @NotNull(message = "É necessário vincular um cliente ao aluguel")
    Long cliente_id,

    @NotNull(message = "É necessário vincular um carro ao aluguel")
    Long carro_id

) {}
