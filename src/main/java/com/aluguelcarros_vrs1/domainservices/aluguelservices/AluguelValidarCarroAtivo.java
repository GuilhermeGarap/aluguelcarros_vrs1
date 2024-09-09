package com.aluguelcarros_vrs1.domainservices.aluguelservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.domain.carro.CarroRepository;
import com.aluguelcarros_vrs1.domainservices.ValidacaoException;

@Component
public class AluguelValidarCarroAtivo implements AluguelValidador {
    
    @Autowired
    private CarroRepository repository;

    public void validar(DadosCadastroAluguel dados) {
        var carroAtivo = repository.findAtivoById(dados.carro_id());
        if (!carroAtivo) {
            throw new ValidacaoException("Esse carro está desativado no sistema");
        }
    }
}
