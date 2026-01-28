package com.aluguelcarros_vrs1.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.aluguelcarros_vrs1.domainservices.ValidacaoException;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.aluguelcarros_vrs1.domain.cliente.Cliente;
import com.aluguelcarros_vrs1.domain.cliente.ClienteRepository;
import com.aluguelcarros_vrs1.domain.cliente.DadosCadastroCliente;
import com.aluguelcarros_vrs1.domain.cliente.DadosDetalhamentoCliente;
import com.aluguelcarros_vrs1.domain.cliente.DadosEditarCliente;
import com.aluguelcarros_vrs1.domain.cliente.DadosListaCliente;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cliente")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Clientes", description = "Endpoints para gerenciar clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository repository;

    @Operation(summary = "Cadastra um novo cliente", description = "Endpoint para registrar um novo cliente")
    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity<DadosDetalhamentoCliente> cadastrar(@RequestBody @Valid DadosCadastroCliente dados, UriComponentsBuilder uriBuilder) {
        var cliente = new Cliente(dados);
        if (!cliente.verificadorCpf(cliente.getCpf())) {
            throw new ValidacaoException("CPF Inválido!");
        }
        repository.save(cliente);

        var uri = uriBuilder.path("/cliente/cadastrar/{id}").buildAndExpand(cliente.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoCliente(cliente));
    }

    @Operation(summary = "Lista todos os clientes ativos", description = "Endpoint para listar todos os clientes ativos sem paginação")
    @GetMapping("/listar")
    public ResponseEntity<List<DadosListaCliente>> listar() {
        List<DadosListaCliente> clientesAtivos = repository.findAllByAtivoTrue()
            .stream()
            .map(DadosListaCliente::new) 
            .collect(Collectors.toList()); 
        return ResponseEntity.ok(clientesAtivos);
    }

    @Operation(summary = "Atualiza um cliente", description = "Endpoint para atualizar as informações de um cliente existente")
    @PutMapping("/editar/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoCliente> atualizar(@PathVariable Long id, @RequestBody @Valid DadosEditarCliente dados) {
        var cliente = repository.getReferenceById(id);
        cliente.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoCliente(cliente));
    }

    @Operation(summary = "Desativa um cliente", description = "Endpoint para desativar um cliente")
    @DeleteMapping("/desativar/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoCliente> desativar(@PathVariable Long id) {
        var cliente = repository.getReferenceById(id);
        cliente.desativar();
        repository.save(cliente); // Salvando o cliente desativado

        return ResponseEntity.ok(new DadosDetalhamentoCliente(cliente));
    }

    @Operation(summary = "Ativa um cliente", description = "Endpoint para ativar um cliente")
    @PatchMapping("/ativar/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoCliente> ativar(@PathVariable Long id) {
        var cliente = repository.getReferenceById(id);
        if (!cliente.getAtivo()) {
            cliente.ativar();
            repository.save(cliente); // Salvando o cliente ativado
            return ResponseEntity.ok(new DadosDetalhamentoCliente(cliente));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @Operation(summary = "Busca um cliente pelo ID", description = "Endpoint para buscar um cliente pelo ID")
    @GetMapping("/buscar/{id}")
    public ResponseEntity<DadosDetalhamentoCliente> buscar(@PathVariable Long id) {
        var cliente = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoCliente(cliente));
    }
}
