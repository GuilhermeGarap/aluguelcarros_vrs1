package com.aluguelcarros_vrs1.domain.cliente;

import com.aluguelcarros_vrs1.domain.endereco.DadosEndereco;

import java.time.LocalDate;

public record DadosEditarCliente(
    String nome,
    String telefone,
    LocalDate dataNascimento,
    DadosEndereco endereco
) {
    
}
