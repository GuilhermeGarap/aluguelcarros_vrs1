package com.aluguelcarros_vrs1.domainservices.aluguelservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.domain.cliente.ClienteRepository;
import com.aluguelcarros_vrs1.domainservices.ValidacaoException;

@Component
public class AluguelValidarClienteAtivo implements AluguelValidador {

    @Autowired
    private ClienteRepository repository;

    public void validar(DadosCadastroAluguel dados) {
        var clienteAtivo = repository.findAtivoById(dados.cliente_id());
        System.out.println("Verificando se o cliente está ativo: " + clienteAtivo);
        if (!clienteAtivo) {
            throw new ValidacaoException("Esse cliente está desativado no sistema");
        }
    }
    
    
}
