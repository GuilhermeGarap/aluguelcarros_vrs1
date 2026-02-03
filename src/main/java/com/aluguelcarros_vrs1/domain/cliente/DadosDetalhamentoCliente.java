package com.aluguelcarros_vrs1.domain.cliente;

import com.aluguelcarros_vrs1.domain.endereco.Endereco;

import java.time.LocalDate;

public record DadosDetalhamentoCliente(
    Long id,
    String nome,
    String email,
    String telefone,
    String cpf,
    LocalDate dataNascimento,
    Endereco endereco,
    Boolean ativo) {

    public DadosDetalhamentoCliente(Cliente cliente) {
        this(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getTelefone(), cliente.getCpf(), cliente.getDataNascimento(), cliente.getEndereco(), cliente.getAtivo());
    
    }
}

