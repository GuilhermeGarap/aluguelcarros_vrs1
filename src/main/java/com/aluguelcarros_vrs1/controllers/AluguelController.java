package com.aluguelcarros_vrs1.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aluguelcarros_vrs1.domain.aluguel.Aluguel;
import com.aluguelcarros_vrs1.domain.aluguel.AluguelRepository;
import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.domain.aluguel.DadosDetalhamentoAluguel;
import com.aluguelcarros_vrs1.domain.aluguel.DadosEditarAluguel;
import com.aluguelcarros_vrs1.domain.aluguel.DadosListaAluguel;
import com.aluguelcarros_vrs1.domainservices.ValidacaoException;
import com.aluguelcarros_vrs1.domainservices.aluguelservices.AluguelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/aluguel")
@SecurityRequirement(name = "bearer-key")
@Tag(name = " Aluguel", description = "Gerenciamento de aluguéis de carros")
public class AluguelController {
    
    @Autowired
    private AluguelRepository repository;

    @Autowired
    private AluguelService aluguelService;
    
    @Operation(summary = "Cadastra um novo aluguel", description = "Endpoint para registrar um novo aluguel de carro")
    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity<DadosDetalhamentoAluguel> cadastrar(@RequestBody @Valid DadosCadastroAluguel dados) {
        var dto = aluguelService.cadastrar(dados);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Lista todos os aluguéis ativos", description = "Retorna uma lista de aluguéis ativos")
    @GetMapping("/listarAtivos")
    public ResponseEntity<List<DadosListaAluguel>> listarAtivos() {
        List<Aluguel> alugueisAtivos = repository.findAllByAtivoTrue();  
        List<DadosListaAluguel> dadosAlugueisAtivos = alugueisAtivos.stream()
            .map(DadosListaAluguel::new) 
            .collect(Collectors.toList());  
        return ResponseEntity.ok(dadosAlugueisAtivos); 
    }

    @Operation(summary = "Lista todos os aluguéis desativados", description = "Retorna uma lista de aluguéis desativados")
    @GetMapping("/listarDesativados")
    public ResponseEntity<List<DadosListaAluguel>> listarDesativados() {
        List<Aluguel> alugueisDesativados = repository.findAllByAtivoFalse();  
        List<DadosListaAluguel> dadosAlugueisDesativados = alugueisDesativados.stream()
            .map(DadosListaAluguel::new) 
            .collect(Collectors.toList());  
        return ResponseEntity.ok(dadosAlugueisDesativados); 
    }

    @Operation(summary = "Lista todos os aluguéis", description = "Retorna uma lista de todos os aluguéis")
    @GetMapping("/listarTodos")
    public ResponseEntity<List<DadosListaAluguel>> listarTodos() {
        List<Aluguel> todosAlugueis = repository.findAll();  
        List<DadosListaAluguel> dadosTodosAlugueis = todosAlugueis.stream()
            .map(DadosListaAluguel::new) 
            .collect(Collectors.toList());  
        return ResponseEntity.ok(dadosTodosAlugueis); 
    }



    @Operation(summary = "Atualiza um aluguel", description = "Atualiza as informações de um aluguel existente")
    @PutMapping("/editar/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoAluguel> atualizar(@PathVariable Long id, @RequestBody DadosEditarAluguel dados) {
        var aluguel = repository.getReferenceById(id);
        aluguel.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoAluguel(aluguel));
    }

    @Operation(summary = "Ativa um aluguel", description = "Ativa um aluguel desativado")
    @PatchMapping("/ativar/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoAluguel> ativar(@PathVariable Long id) {
        var aluguel = repository.getReferenceById(id);
        if (aluguel.getAtivo() == false) {
            aluguel.ativar();
            return ResponseEntity.ok(new DadosDetalhamentoAluguel(aluguel));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @Operation(summary = "Busca um aluguel pelo ID", description = "Retorna um aluguel com base no ID fornecido")
    @GetMapping("/buscar/{id}")
    public ResponseEntity<DadosDetalhamentoAluguel> buscar(@PathVariable Long id) {
        var aluguel = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoAluguel(aluguel));
    }

    @Operation(summary = "Desativa um aluguel", description = "Desativa um aluguel e incrementa a disponibilidade do carro associado")
    @DeleteMapping("/desativar/{id}")
    public ResponseEntity<Object> desativar(@PathVariable Long id) {
        try {
            Aluguel aluguel = aluguelService.desativarAluguel(id);
            return ResponseEntity.ok(new DadosDetalhamentoAluguel(aluguel));
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
