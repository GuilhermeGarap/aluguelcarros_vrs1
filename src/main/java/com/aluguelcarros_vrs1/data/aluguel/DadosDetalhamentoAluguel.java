package com.aluguelcarros_vrs1.data.aluguel;

import com.aluguelcarros_vrs1.domain.Aluguel;

import java.time.Instant;
import java.time.LocalDate;

public record DadosDetalhamentoAluguel(
    Long id,
    LocalDate dataInicio,
    LocalDate dataTermino,
    Long clienteId,
    String cpf,
    String nome,
    Long carroId,
    String modelo,
    Instant createdAt,
    Instant updatedAt,
    String createdBy


) {
    public DadosDetalhamentoAluguel(Aluguel aluguel) {
        this(aluguel.getId(), aluguel.getDataInicio(), aluguel.getDataTermino(),
                aluguel.getCliente().getId(), aluguel.getCliente().getCpf(), aluguel.getCliente().getNome(),
                aluguel.getCarro().getId(), aluguel.getCarro().getModelo(), aluguel.getCreatedAt(),
                aluguel.getUpdatedAt(), aluguel.getCreatedBy());
    }
}
