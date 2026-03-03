package com.aluguelcarros_vrs1.services.aluguelservices;

import com.aluguelcarros_vrs1.data.aluguel.DadosDetalhamentoAluguel;
import com.aluguelcarros_vrs1.domain.Aluguel;
import com.aluguelcarros_vrs1.infra.exception.ErrorDetailsException;
import com.aluguelcarros_vrs1.repositories.AluguelRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AluguelServiceStatus {

    @Autowired
    AluguelRepository aluguelRepository;

    public Aluguel procurarPorId(Long id) {
        return aluguelRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Não existe um aluguel com esse ID"));
    }

    @Transactional
    public DadosDetalhamentoAluguel ativar(Long id) {
        var aluguel = procurarPorId(id);
        aluguel.ativar();
        return new DadosDetalhamentoAluguel(aluguel);
    }

    @Transactional
    public DadosDetalhamentoAluguel desativar(Long id) {
        var aluguel = procurarPorId(id);

        if (aluguel.getCarro().getDisponivel() <= aluguel.getCarro().getUnidades()) {
            aluguel.getCarro().setDisponivel(aluguel.getCarro().getDisponivel() + 1);
        } else {
            throw new ErrorDetailsException("A quantidade de carros totais em estoque não pode ser menor que a quantidade disponível. Total: " +
                    aluguel.getCarro().getUnidades() + ", quantidade disponível no momento: " + aluguel.getCarro().getDisponivel());
        }

        aluguel.desativar();
        return new DadosDetalhamentoAluguel(aluguel);
    }

    @Transactional
    public DadosDetalhamentoAluguel pendente(Long id) {
        var aluguel = procurarPorId(id);

        aluguel.pendente();
        return new DadosDetalhamentoAluguel(aluguel);
    }

    @Transactional
    public DadosDetalhamentoAluguel encerrar(Long id) {
        var aluguel = procurarPorId(id);

        if (aluguel.getCarro().getDisponivel() <= aluguel.getCarro().getUnidades()) {
            aluguel.getCarro().setDisponivel(aluguel.getCarro().getDisponivel() + 1);
        } else {
            throw new ErrorDetailsException("A quantidade de carros totais em estoque não pode ser menor que a quantidade disponível. Total: " +
                    aluguel.getCarro().getUnidades() + ", quantidade disponível no momento: " + aluguel.getCarro().getDisponivel());
        }

        aluguel.encerrar();
        return new DadosDetalhamentoAluguel(aluguel);
    }

    @Transactional
    public DadosDetalhamentoAluguel cancelar(Long id) {
        var aluguel = procurarPorId(id);

        if (aluguel.getCarro().getDisponivel() <= aluguel.getCarro().getUnidades()) {
            aluguel.getCarro().setDisponivel(aluguel.getCarro().getDisponivel() + 1);
        } else {
            throw new ErrorDetailsException("A quantidade de carros totais em estoque não pode ser menor que a quantidade disponível. Total: " +
                    aluguel.getCarro().getUnidades() + ", quantidade disponível no momento: " + aluguel.getCarro().getDisponivel());
        }

        aluguel.cancelar();
        return new DadosDetalhamentoAluguel(aluguel);
    }
}
