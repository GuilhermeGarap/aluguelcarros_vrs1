package com.aluguelcarros_vrs1.controllers.docs;

import com.aluguelcarros_vrs1.data.cliente.DadosCadastroCliente;
import com.aluguelcarros_vrs1.data.cliente.DadosDetalhamentoCliente;
import com.aluguelcarros_vrs1.data.cliente.DadosEditarCliente;
import com.aluguelcarros_vrs1.data.cliente.DadosListaCliente;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public interface ClienteControllerDoc {
    @Operation(summary = "Cadastra um novo cliente", description = "Endpoint para registrar um novo cliente")
    ResponseEntity<DadosDetalhamentoCliente> cadastrar(DadosCadastroCliente dados, UriComponentsBuilder uriBuilder);

    @Operation(summary = "Lista todos os clientes ativos", description = "Endpoint para listar todos os clientes ativos sem paginação")
    ResponseEntity<List<DadosListaCliente>> listar();

    @Operation(summary = "Atualiza um cliente", description = "Endpoint para atualizar as informações de um cliente existente")
    ResponseEntity<DadosDetalhamentoCliente> atualizar(Long id, DadosEditarCliente dados);

    @Operation(summary = "Desativa um cliente", description = "Endpoint para desativar um cliente")
    ResponseEntity<DadosDetalhamentoCliente> desativar(Long id);

    @Operation(summary = "Ativa um cliente", description = "Endpoint para ativar um cliente")
    ResponseEntity<DadosDetalhamentoCliente> ativar(Long id);

    @Operation(summary = "Busca um cliente pelo ID", description = "Endpoint para buscar um cliente pelo ID")
    ResponseEntity<DadosDetalhamentoCliente> buscar(Long id);
}
