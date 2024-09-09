package com.aluguelcarros_vrs1.domainservices.aluguelservices;

import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;

public interface AluguelValidador {
    
    void validar(DadosCadastroAluguel dados);
}
