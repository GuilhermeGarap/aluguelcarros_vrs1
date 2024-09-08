package com.aluguelcarros_vrs1.domain.aluguel;

import java.time.LocalDate;

import com.aluguelcarros_vrs1.domain.carro.DadosListaCarro;
import com.aluguelcarros_vrs1.domain.cliente.DadosListaCliente;

public record DadosListaAluguel(
    Long id,
    LocalDate data_inicio,
    LocalDate data_termino,
    Boolean ativo,
    DadosListaCarro carro,
    DadosListaCliente cliente
) {
    // Construtor customizado para criar um DadosListaAluguel a partir de um objeto Aluguel
    public DadosListaAluguel(Aluguel aluguel) {
        this(
            aluguel.getId(),
            aluguel.getData_inicio(),
            aluguel.getData_termino(),
            aluguel.getAtivo(),
            new DadosListaCarro(aluguel.getCarro()),
            new DadosListaCliente(aluguel.getCliente())
        );
    }
}
