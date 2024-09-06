package com.aluguelcarros_vrs1.domain.carro;

public record DadosListaCarro(
Long id,
String modelo,
Float valor_dia,
Integer unidades,
Integer disponivel
) {
    public DadosListaCarro(Carro carro){
        this(carro.getId(), carro.getModelo(), carro.getValor_dia(), carro.getUnidades(), carro.getDisponivel());
    }
}
