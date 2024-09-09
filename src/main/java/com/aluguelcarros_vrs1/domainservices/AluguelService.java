package com.aluguelcarros_vrs1.domainservices;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.aluguelcarros_vrs1.domain.aluguel.Aluguel;
import com.aluguelcarros_vrs1.domain.aluguel.AluguelRepository;

@Service
public class AluguelService {

    @Autowired
    private AluguelRepository repository;

    // Agendar para rodar todos os dias às 13:00 (horário de Brasília)
    @Scheduled(cron = "0 0 13 * * ?", zone = "America/Sao_Paulo")
    public void desativarAlugueisExpirados() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        LocalDateTime agora = LocalDateTime.now(zoneId);

        // Buscar todos os aluguéis ativos
        var alugueis = repository.findAllByAtivoTrue();

        for (Aluguel aluguel : alugueis) {
            // Comparar o término do aluguel com a data e hora atual
            LocalDateTime termino = aluguel.getData_termino().atTime(13, 0);
            if (termino.isBefore(agora) || termino.isEqual(agora)) {
                aluguel.desativar();
                repository.save(aluguel); // Desativa o aluguel e salva no banco
            }
        }
    }
}
