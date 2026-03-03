package com.aluguelcarros_vrs1.services.aluguelservices;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import com.aluguelcarros_vrs1.domain.User;
import com.aluguelcarros_vrs1.infra.exception.NotFoundException;
import com.aluguelcarros_vrs1.services.aluguelservices.Validadores.AluguelValidador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.aluguelcarros_vrs1.domain.Aluguel;
import com.aluguelcarros_vrs1.repositories.AluguelRepository;
import com.aluguelcarros_vrs1.data.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.data.aluguel.DadosDetalhamentoAluguel;
import com.aluguelcarros_vrs1.data.aluguel.DadosEditarAluguel;
import com.aluguelcarros_vrs1.data.aluguel.DadosListaAluguel;
import com.aluguelcarros_vrs1.repositories.CarroRepository;
import com.aluguelcarros_vrs1.repositories.ClienteRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class AluguelService {

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private List<AluguelValidador> validadores;

    @Autowired
    private AluguelServiceStatus aluguelServiceStatus;

    @Scheduled(cron = "0 0 13 * * ?", zone = "America/Sao_Paulo")
    @Transactional
    public void desativarAlugueisExpirados() {
        LocalDate hoje = LocalDate.now(ZoneId.of("America/Sao_Paulo"));

        List<Aluguel> expirados = aluguelRepository.findExpirados(hoje);

        if (!expirados.isEmpty()) {
            expirados.forEach(Aluguel::encerrar);
            aluguelRepository.saveAll(expirados);
        }
    }

    @Transactional
    public DadosDetalhamentoAluguel cadastrar(@RequestBody @Valid DadosCadastroAluguel dados) {
        if (!clienteRepository.existsById(dados.cliente_id())) {
            throw new NotFoundException("ID do Cliente informado não existe!");
        }

        if (!carroRepository.existsById(dados.carro_id())) {
            throw new NotFoundException("ID do Carro informado não existe!");
        }
        var cliente = clienteRepository.getReferenceById(dados.cliente_id());
        var carro = carroRepository.getReferenceById(dados.carro_id());
        
        validadores.forEach(v -> v.validar(dados));

        var dadosDTO = new DadosCadastroAluguel(dados.dataInicio(), dados.dataTermino(), cliente.getId(), carro.getId());
        var aluguel = new Aluguel(dadosDTO);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        aluguel.setCreatedBy(user.getUsername());

        aluguelRepository.save(aluguel);
        return new DadosDetalhamentoAluguel(aluguel);
    }

    public List<DadosListaAluguel> listarAtivos() {
        List<Aluguel> alugueisAtivos = aluguelRepository.findAtivosAndPendentes();
        return  alugueisAtivos.stream()
                .map(DadosListaAluguel::new)
                .collect(Collectors.toList());
    }

    public List<DadosListaAluguel> listarDesativados() {
        List<Aluguel> alugueisDesativados = aluguelRepository.findInativosAndEncerradosAndCancelados();
        return alugueisDesativados.stream()
                .map(DadosListaAluguel::new)
                .collect(Collectors.toList());
    }

    public List<DadosListaAluguel> listarTodos() {
        List<Aluguel> todosAlugueis = aluguelRepository.findAll();
        return todosAlugueis.stream()
                .map(DadosListaAluguel::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Aluguel atualizar(Long id, DadosEditarAluguel dados) {
        var aluguel = aluguelRepository.getReferenceById(id);
        aluguel.atualizarInformacoes(dados);
        return aluguel;
    }

    public DadosDetalhamentoAluguel buscar(Long id) {
        var aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Não existe um aluguel com esse ID"));
        return new DadosDetalhamentoAluguel(aluguel);
    }
}
