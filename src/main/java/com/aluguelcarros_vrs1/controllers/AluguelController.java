package com.aluguelcarros_vrs1.controllers;

import java.util.List;

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
        List<DadosListaAluguel> dadosAlugueisAtivos = aluguelService.listarAtivos();
        return ResponseEntity.ok(dadosAlugueisAtivos);
    }

    @Operation(summary = "Lista todos os aluguéis desativados", description = "Retorna uma lista de aluguéis desativados")
    @GetMapping("/listarDesativados")
    public ResponseEntity<List<DadosListaAluguel>> listarDesativados() {
        List<DadosListaAluguel> dadosAlugueisDesativados = aluguelService.listarDesativados();
        return ResponseEntity.ok(dadosAlugueisDesativados); 
    }

    @Operation(summary = "Lista todos os aluguéis", description = "Retorna uma lista de todos os aluguéis")
    @GetMapping("/listarTodos")
    public ResponseEntity<List<DadosListaAluguel>> listarTodos() {
        List<DadosListaAluguel> dadosTodosAlugueis = aluguelService.listarTodos();
        return ResponseEntity.ok(dadosTodosAlugueis); 
    }

    @Operation(summary = "Atualiza um aluguel", description = "Atualiza as informações de um aluguel existente")
    @PutMapping("/editar/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoAluguel> atualizar(@PathVariable Long id, @RequestBody DadosEditarAluguel dados) {
        var aluguel = aluguelService.atualizar(id, dados);
        return ResponseEntity.ok(new DadosDetalhamentoAluguel(aluguel));
    }

    @Operation(summary = "Ativa um aluguel", description = "Ativa um aluguel desativado")
    @PatchMapping("/ativar/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoAluguel> ativar(@PathVariable Long id) {
        var dto = aluguelService.ativar(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @Operation(summary = "Busca um aluguel pelo ID", description = "Retorna um aluguel com base no ID fornecido")
    @GetMapping("/buscar/{id}")
    public ResponseEntity<DadosDetalhamentoAluguel> buscar(@PathVariable Long id) {
        var dto = aluguelService.buscar(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Desativa um aluguel", description = "Desativa um aluguel e incrementa a disponibilidade do carro associado")
    @DeleteMapping("/desativar/{id}")
    public ResponseEntity<Object> desativar(@PathVariable Long id) {
        try {
            var dto = aluguelService.desativarAluguel(id);
            return ResponseEntity.ok(dto);
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
