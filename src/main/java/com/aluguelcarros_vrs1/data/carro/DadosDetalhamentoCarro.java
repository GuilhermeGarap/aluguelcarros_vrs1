package com.aluguelcarros_vrs1.data.carro;

import com.aluguelcarros_vrs1.domain.Carro;

import java.time.Instant;

public record DadosDetalhamentoCarro(
    Long id,
    String modelo,
    Float valor_dia,
    Integer unidades,
    Integer disponivel,
    Boolean ativo,
    Instant createdAt,
    Instant updatedAt,
    String createdBy
) {
    public DadosDetalhamentoCarro(Carro carro) {
        this(carro.getId(), carro.getModelo(), carro.getValorDia(), carro.getUnidades(), carro.getDisponivel(), carro.getAtivo(),
                carro.getCreatedAt(), carro.getUpdatedAt(), carro.getCreatedBy());
    }
}
