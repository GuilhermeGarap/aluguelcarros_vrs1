package com.aluguelcarros_vrs1.domain.aluguel;

import java.time.LocalDate;

public record DadosDetalhamentoAluguel(
    Long id,
    LocalDate data_inicio,
    LocalDate data_termino,
    Boolean ativo


) {
    public DadosDetalhamentoAluguel(Aluguel aluguel) {
        this(aluguel.getId(), aluguel.getData_inicio(), aluguel.getData_termino(), aluguel.getAtivo());
    }
}
