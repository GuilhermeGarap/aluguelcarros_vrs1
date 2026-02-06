package com.aluguelcarros_vrs1.domainservices.aluguelservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.repositories.CarroRepository;
import com.aluguelcarros_vrs1.infra.exception.ValidacaoException;

@Component
public class AluguelLogicaCarroDisponivel implements AluguelValidador {

    @Autowired
    private CarroRepository carroRepository;

    @Override
    public void validar(DadosCadastroAluguel dados) {
        var carro = carroRepository.getReferenceById(dados.carro_id());
        
        // Verifica a disponibilidade do carro
        if (carro.getDisponivel() <= 0) {
            throw new ValidacaoException("Não há carros disponíveis para este modelo.");
        }

        carro.setDisponivel(carro.getDisponivel() - 1);
        carroRepository.save(carro);
    }
}
