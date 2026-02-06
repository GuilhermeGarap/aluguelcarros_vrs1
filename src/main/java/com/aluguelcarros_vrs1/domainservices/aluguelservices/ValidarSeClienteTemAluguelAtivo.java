package com.aluguelcarros_vrs1.domainservices.aluguelservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.domain.cliente.Cliente;
import com.aluguelcarros_vrs1.repositories.ClienteRepository;
import com.aluguelcarros_vrs1.infra.exception.ValidacaoException;

@Component
public class ValidarSeClienteTemAluguelAtivo implements AluguelValidador {
    
    @Autowired
    private ClienteRepository repository;

    @Override
    @SuppressWarnings("null")
    public void validar(DadosCadastroAluguel dados) {
        Cliente cliente = repository.findById(dados.cliente_id())
            .orElseThrow(() -> new ValidacaoException("Cliente não encontrado"));

        boolean temAluguelAtivo = cliente.getAlugueis().stream()
            .anyMatch(aluguel -> aluguel.getAtivo());
        
        if (temAluguelAtivo) {
            throw new ValidacaoException("Este cliente já possui um aluguel ativo. Finalize o aluguel anterior antes de criar um novo.");
        }
    }
    
}
