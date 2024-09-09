package com.aluguelcarros_vrs1.domainservices.aluguelservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aluguelcarros_vrs1.domain.aluguel.AluguelRepository;
import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.domain.carro.Carro;
import com.aluguelcarros_vrs1.domain.carro.CarroRepository;
import com.aluguelcarros_vrs1.domain.cliente.ClienteRepository;
import com.aluguelcarros_vrs1.domainservices.ValidacaoException;

import jakarta.transaction.Transactional;

@Component
public class AluguelLogicaCarroDisponivel {

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private ClienteRepository clienteRepository; 

    @Autowired
    private AluguelRepository aluguelRepository; 

    @Transactional
    public Carro validar(DadosCadastroAluguel dados) {

        var carro = carroRepository.getReferenceById(dados.carro_id());
        // Verifica a disponibilidade do carro
        if (carro.getDisponivel() <= 0) {
            throw new ValidacaoException("Não há carros disponíveis para este modelo.");
        }

        carro.setDisponivel(carro.getDisponivel() - 1);
        return carro;
 
    }
}
