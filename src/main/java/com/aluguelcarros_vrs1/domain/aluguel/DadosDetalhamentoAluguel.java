package com.aluguelcarros_vrs1.domain.aluguel;

public record DadosDetalhamentoAluguel(
    Long id,
    Integer dias_alugados,
    String data_inicio,
    Boolean ativo


) {
    public DadosDetalhamentoAluguel(Aluguel aluguel) {
        this(aluguel.getId(), aluguel.getDias_alugados(), aluguel.getData_inicio(), aluguel.getAtivo());
    }
}
