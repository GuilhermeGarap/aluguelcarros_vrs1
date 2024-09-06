package com.aluguelcarros_vrs1.domain.aluguel;

import com.aluguelcarros_vrs1.domain.carro.DadosListaCarro;
import com.aluguelcarros_vrs1.domain.cliente.DadosListaCliente;

public record DadosListaAluguel(
    Long id,
    Integer dias_alugados,
    String data_inicio,
    DadosListaCarro carro,
    DadosListaCliente cliente
) {
    // Construtor customizado para criar um DadosListaAluguel a partir de um objeto Aluguel
    public DadosListaAluguel(Aluguel aluguel) {
        this(
            aluguel.getId(),
            aluguel.getDias_alugados(),
            aluguel.getData_inicio(),
            new DadosListaCarro(aluguel.getCarro()),
            new DadosListaCliente(aluguel.getCliente())
        );
    }
}
