package com.aluguelcarros_vrs1.domain.cliente;

import com.aluguelcarros_vrs1.domain.endereco.DadosEndereco;

public record DadosEditarCliente(
    String nome,
    String telefone,
    DadosEndereco endereco
) {
    
}
