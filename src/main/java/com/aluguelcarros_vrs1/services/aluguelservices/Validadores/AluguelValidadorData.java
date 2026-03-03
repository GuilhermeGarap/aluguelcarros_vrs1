package com.aluguelcarros_vrs1.services.aluguelservices.Validadores;

import java.time.DayOfWeek;

import com.aluguelcarros_vrs1.infra.exception.ErrorDetailsException;
import org.springframework.stereotype.Component;

import com.aluguelcarros_vrs1.data.aluguel.DadosCadastroAluguel;

@Component
public class AluguelValidadorData implements AluguelValidador {
    
    @Override
    public void validar(DadosCadastroAluguel dados) {
    boolean domingo = dados.dataInicio().getDayOfWeek().equals(DayOfWeek.SUNDAY) ||
                              dados.dataTermino().getDayOfWeek().equals(DayOfWeek.SUNDAY);

    if (domingo) {
        throw new ErrorDetailsException("Data de Início ou Término não pode ser no domingo");
    }
}
}