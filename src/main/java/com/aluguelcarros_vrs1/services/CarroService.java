package com.aluguelcarros_vrs1.services;

import java.util.List;
import java.util.stream.Collectors;

import com.aluguelcarros_vrs1.domain.User;
import com.aluguelcarros_vrs1.infra.exception.ErrorDetailsException;
import com.aluguelcarros_vrs1.infra.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.aluguelcarros_vrs1.domain.Carro;
import com.aluguelcarros_vrs1.repositories.CarroRepository;
import com.aluguelcarros_vrs1.data.carro.DadosCadastroCarro;
import com.aluguelcarros_vrs1.data.carro.DadosDetalhamentoCarro;
import com.aluguelcarros_vrs1.data.carro.DadosEditarCarro;
import com.aluguelcarros_vrs1.data.carro.DadosListaCarro;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;

    @Transactional
    public DadosDetalhamentoCarro cadastrar(@Valid DadosCadastroCarro dados) {
        if (carroRepository.existsByModelo(dados.modelo())) {
            throw new ErrorDetailsException("Esse nome de modelo de carro já está registrado!");
        }
        var carro = new Carro(dados);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        carro.setCreatedBy(user.getUsername());

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
            throw new ErrorDetailsException("Algum outro carro já possui esse nome de modelo");
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
                .orElseThrow(() -> new NotFoundException("Não existe um carro com esse ID"));

        return new DadosDetalhamentoCarro(carro);
    }
}
