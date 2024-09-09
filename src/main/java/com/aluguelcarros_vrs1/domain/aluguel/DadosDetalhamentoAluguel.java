package com.aluguelcarros_vrs1.domain.aluguel;

import java.time.LocalDate;

public record DadosDetalhamentoAluguel(
    Long id,
    LocalDate data_inicio,
    LocalDate data_termino,
    Boolean ativo,
    Long cliente_id,
    String cpf,
    String nome,
    Long carro_id,
    String modelo


) {
    public DadosDetalhamentoAluguel(Aluguel aluguel) {
        this(aluguel.getId(), aluguel.getData_inicio(), aluguel.getData_termino(), aluguel.getAtivo(), 
        aluguel.getCliente().getId(), aluguel.getCliente().getCpf(), aluguel.getCliente().getNome(),
        aluguel.getCarro().getId(), aluguel.getCarro().getModelo());
    }
}
