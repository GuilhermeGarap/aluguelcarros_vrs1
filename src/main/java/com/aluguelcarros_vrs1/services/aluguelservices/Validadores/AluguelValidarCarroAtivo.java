package com.aluguelcarros_vrs1.services.aluguelservices.Validadores;

import com.aluguelcarros_vrs1.infra.exception.ErrorDetailsException;
import com.aluguelcarros_vrs1.services.aluguelservices.Validadores.AluguelValidador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aluguelcarros_vrs1.data.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.repositories.CarroRepository;

@Component
public class AluguelValidarCarroAtivo implements AluguelValidador {
    
    @Autowired
    private CarroRepository repository;

    @Override
    public void validar(DadosCadastroAluguel dados) {
        var carroAtivo = repository.findAtivoById(dados.carro_id());
        if (!carroAtivo) {
            throw new ErrorDetailsException("Esse carro está desativado no sistema");
        }
    }
}
