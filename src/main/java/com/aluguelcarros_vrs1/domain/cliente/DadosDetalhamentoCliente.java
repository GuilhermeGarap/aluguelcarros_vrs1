package com.aluguelcarros_vrs1.domain.cliente;

import com.aluguelcarros_vrs1.domain.endereco.Endereco;

public record DadosDetalhamentoCliente(
    Long id,
    String nome,
    String email,
    String telefone,
    String cpf,
    Endereco endereco,
    Boolean ativo) {

    public DadosDetalhamentoCliente(Cliente cliente) {
        this(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getTelefone(), cliente.getCpf(), cliente.getEndereco(), cliente.getAtivo());
    
    }
}

