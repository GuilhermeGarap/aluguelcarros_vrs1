package com.aluguelcarros_vrs1.domainservices.carroservices;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aluguelcarros_vrs1.domain.carro.Carro;
import com.aluguelcarros_vrs1.repositories.CarroRepository;
import com.aluguelcarros_vrs1.domain.carro.DadosCadastroCarro;
import com.aluguelcarros_vrs1.domain.carro.DadosDetalhamentoCarro;
import com.aluguelcarros_vrs1.domain.carro.DadosEditarCarro;
import com.aluguelcarros_vrs1.domain.carro.DadosListaCarro;
import com.aluguelcarros_vrs1.infra.exception.ValidacaoException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;

    @Transactional
    public DadosDetalhamentoCarro cadastrar(@Valid DadosCadastroCarro dados) {
        if (carroRepository.existsByModelo(dados.modelo())) {
            throw new ValidacaoException("Esse nome de modelo de carro já está registrado!");
        }
        var carro = new Carro(dados);
        carroRepository.save(carro);
        return new DadosDetalhamentoCarro(carro);
    }

    public List<DadosListaCarro> listarAtivos() {
        List<Carro> carrosAtivos = carroRepository.findAllByAtivoTrue();
        return carrosAtivos.stream()
                .map(DadosListaCarro::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public DadosDetalhamentoCarro atualizar(Long id, @Valid DadosEditarCarro dados) {
        var carro = carroRepository.getReferenceById(id);
        if (carroRepository.existsByModelo(dados.modelo())) {
            throw new ValidacaoException("Algum outro carro já possui esse nome de modelo");
        }
        carro.atualizarInformacoes(dados);
        return new DadosDetalhamentoCarro(carro);
    }

    @Transactional
    public DadosDetalhamentoCarro desativar(Long id) {
        var carro = carroRepository.getReferenceById(id);
        carro.desativar();
        carroRepository.save(carro);
        return new DadosDetalhamentoCarro(carro);
    }

    @Transactional
    public DadosDetalhamentoCarro ativar(Long id) {
        var carro = carroRepository.getReferenceById(id);
        if (carro.getAtivo() == false) {
            carro.ativar();
            carroRepository.save(carro);
            return new DadosDetalhamentoCarro(carro);
        }
        return null;
    }

    public DadosDetalhamentoCarro buscar(Long id) {
        var carro = carroRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Não existe um carro com esse ID"));

        return new DadosDetalhamentoCarro(carro);
    }
}
