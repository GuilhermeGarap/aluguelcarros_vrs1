package com.aluguelcarros_vrs1.domain.cliente;

public record DadosListaCliente(
    Long id,
    String nome,
    String email,
    String telefone,
    String cpf
) {
    
    public DadosListaCliente(Cliente cliente){
        this(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getTelefone(), cliente.getCpf());
    }
}
