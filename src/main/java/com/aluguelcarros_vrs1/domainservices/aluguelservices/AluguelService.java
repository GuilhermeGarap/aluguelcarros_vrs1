package com.aluguelcarros_vrs1.domainservices.aluguelservices;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.aluguelcarros_vrs1.domain.aluguel.Aluguel;
import com.aluguelcarros_vrs1.domain.aluguel.AluguelRepository;
import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.domain.aluguel.DadosDetalhamentoAluguel;
import com.aluguelcarros_vrs1.domain.carro.Carro;
import com.aluguelcarros_vrs1.domain.carro.CarroRepository;
import com.aluguelcarros_vrs1.domain.cliente.ClienteRepository;
import com.aluguelcarros_vrs1.domainservices.ValidacaoException;

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
    private AluguelLogicaCarroDisponivel logica;

    @Autowired
    private List<AluguelValidador> validadores;

    // Agendar para rodar todos os dias às 13:00 (horário de Brasília)
    @Scheduled(cron = "0 0 13 * * ?", zone = "America/Sao_Paulo")
    public void desativarAlugueisExpirados() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        LocalDateTime agora = LocalDateTime.now(zoneId);

        // Buscar todos os aluguéis ativos
        var alugueis = aluguelRepository.findAllByAtivoTrue();

        for (Aluguel aluguel : alugueis) {
            // Comparar o término do aluguel com a data e hora atual
            LocalDateTime termino = aluguel.getData_termino().atTime(13, 0);
            if (termino.isBefore(agora) || termino.isEqual(agora)) {
                aluguel.desativar();
                aluguelRepository.save(aluguel); // Desativa o aluguel e salva no banco
            }
        }
    }

    @Transactional
    public DadosDetalhamentoAluguel cadastrar(@RequestBody @Valid DadosCadastroAluguel dados) {
         
        if (!clienteRepository.existsById(dados.cliente_id())) {
            throw new ValidacaoException("ID do Cliente informado não existe!");
        }

        if (!carroRepository.existsById(dados.carro_id())) {
            throw new ValidacaoException("ID do Carro informado não existe!");
        }
        var cliente = clienteRepository.getReferenceById(dados.cliente_id());
        
        var carro = logica.validar(dados);
        validadores.forEach(v -> v.validar(dados));

        var aluguel = new Aluguel(null, dados.data_inicio(), dados.data_termino(), true , cliente, carro);
        aluguelRepository.save(aluguel);
        return new DadosDetalhamentoAluguel(aluguel);
    }

    @Transactional
    public Aluguel desativarAluguel(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
            .orElseThrow(() -> new ValidacaoException("Aluguel não encontrado com ID " + id));

        // Incrementa a disponibilidade do carro
        Carro carro = aluguel.getCarro();
        if (carro.getDisponivel() < carro.getUnidades()) {
            carro.setDisponivel(carro.getDisponivel() + 1);
        } else {
            throw new ValidacaoException("Não é possível devolver um carro mais vezes que a quantidade disponivel em estoque.");
        }
        carroRepository.save(carro);

        // Desativa o aluguel
        aluguel.desativar();
        aluguelRepository.save(aluguel);

        return aluguel;
    }
}
