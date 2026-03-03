package com.aluguelcarros_vrs1.services.aluguelservices.Validadores;

import com.aluguelcarros_vrs1.infra.exception.ErrorDetailsException;
import com.aluguelcarros_vrs1.services.aluguelservices.Validadores.AluguelValidador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aluguelcarros_vrs1.data.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.repositories.CarroRepository;

@Component
public class AluguelLogicaCarroDisponivel implements AluguelValidador {

    @Autowired
    private CarroRepository carroRepository;

    @Override
    public void validar(DadosCadastroAluguel dados) {
        var carro = carroRepository.getReferenceById(dados.carro_id());

        if (carro.getDisponivel() <= 0) {
            throw new ErrorDetailsException("Não há carros disponíveis para este modelo.");
        }

        carro.setDisponivel(carro.getDisponivel() - 1);
        carroRepository.save(carro);
    }
}
