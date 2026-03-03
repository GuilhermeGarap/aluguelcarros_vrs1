package com.aluguelcarros_vrs1.services.aluguelservices.Validadores;

import com.aluguelcarros_vrs1.domain.Aluguel;
import com.aluguelcarros_vrs1.infra.exception.ErrorDetailsException;
import com.aluguelcarros_vrs1.infra.exception.NotFoundException;
import com.aluguelcarros_vrs1.repositories.ClienteRepository;
import com.aluguelcarros_vrs1.services.aluguelservices.Validadores.AluguelValidador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aluguelcarros_vrs1.data.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.domain.Cliente;

@Component
public class ValidarSeClienteTemAluguelAtivo implements AluguelValidador {
    
    @Autowired
    private ClienteRepository repository;

    @Override
    @SuppressWarnings("null")
    public void validar(DadosCadastroAluguel dados) {
        Cliente cliente = repository.findById(dados.cliente_id())
            .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        boolean temAluguelAtivo = cliente.getAlugueis().stream()
            .anyMatch(aluguel -> aluguel.getAluguelStatus().contains(Aluguel.AluguelStatus.ATIVO));
        
        if (temAluguelAtivo) {
            throw new ErrorDetailsException("Este cliente já possui um aluguel ativo. Finalize o aluguel anterior antes de criar um novo.");
        }
    }
    
}
