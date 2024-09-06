package com.aluguelcarros_vrs1.domain.carro;

public record DadosDetalhamentoCarro(
    Long id,
    String modelo,
    Float valor_dia,
    Integer unidades,
    Integer disponivel,
    Boolean ativo
) {
    public DadosDetalhamentoCarro(Carro carro) {
        this(carro.getId(), carro.getModelo(), carro.getValor_dia(), carro.getUnidades(), carro.getDisponivel(), carro.getAtivo());
    }
}
