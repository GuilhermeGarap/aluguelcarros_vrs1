package com.aluguelcarros_vrs1.data.carro;

import com.aluguelcarros_vrs1.domain.Carro;

public record DadosListaCarro(
Long id,
String modelo,
Float valor_dia,
Integer unidades,
Integer disponivel,
Boolean ativo
) {
    public DadosListaCarro(Carro carro){
        this(carro.getId(), carro.getModelo(), carro.getValorDia(), carro.getUnidades(), carro.getDisponivel(), carro.getAtivo());
    }
}
