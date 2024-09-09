package com.aluguelcarros_vrs1.domainservices.aluguelservices;

import java.time.DayOfWeek;

import org.springframework.stereotype.Component;

import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.domainservices.ValidacaoException;

@Component
public class AluguelValidadorData implements AluguelValidador{
    
    public void validar(DadosCadastroAluguel dados) {
    // Verifica se qualquer uma das datas é um domingo
    boolean domingo = dados.data_inicio().getDayOfWeek().equals(DayOfWeek.SUNDAY) ||
                              dados.data_termino().getDayOfWeek().equals(DayOfWeek.SUNDAY);

    if (domingo) {
        throw new ValidacaoException("Data de Início ou Término não pode ser no domingo");
    }
}
}