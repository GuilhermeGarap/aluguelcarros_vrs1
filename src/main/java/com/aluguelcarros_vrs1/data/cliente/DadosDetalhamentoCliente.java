package com.aluguelcarros_vrs1.data.cliente;

import com.aluguelcarros_vrs1.domain.Cliente;
import com.aluguelcarros_vrs1.domain.Endereco;

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

