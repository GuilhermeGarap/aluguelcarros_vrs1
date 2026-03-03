package com.aluguelcarros_vrs1.data.aluguel;

import java.time.LocalDate;

import com.aluguelcarros_vrs1.domain.Aluguel;
import com.aluguelcarros_vrs1.data.carro.DadosListaCarro;
import com.aluguelcarros_vrs1.data.cliente.DadosListaCliente;

public record DadosListaAluguel(
    Long id,
    LocalDate dataInicio,
    LocalDate dataTermino,
    DadosListaCarro carro,
    DadosListaCliente cliente
) {
    public DadosListaAluguel(Aluguel aluguel) {
        this(
            aluguel.getId(),
            aluguel.getDataInicio(),
            aluguel.getDataTermino(),
            new DadosListaCarro(aluguel.getCarro()),
            new DadosListaCliente(aluguel.getCliente())
        );
    }
}
