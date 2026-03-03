package com.aluguelcarros_vrs1.services.aluguelservices.Validadores;

import com.aluguelcarros_vrs1.infra.exception.ErrorDetailsException;
import com.aluguelcarros_vrs1.services.aluguelservices.Validadores.AluguelValidador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aluguelcarros_vrs1.data.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.repositories.ClienteRepository;

@Component
public class AluguelValidarClienteAtivo implements AluguelValidador {

    @Autowired
    private ClienteRepository repository;

    @Override
    public void validar(DadosCadastroAluguel dados) {
        var clienteAtivo = repository.findAtivoById(dados.cliente_id());
        if (!clienteAtivo) {
            throw new ErrorDetailsException("Esse cliente está desativado no sistema");
        }
    }
    
    
}
