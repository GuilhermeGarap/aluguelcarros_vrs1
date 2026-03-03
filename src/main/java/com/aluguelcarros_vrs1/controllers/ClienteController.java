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
import org.springframework.web.util.UriComponentsBuilder;

import com.aluguelcarros_vrs1.data.cliente.DadosCadastroCliente;
import com.aluguelcarros_vrs1.data.cliente.DadosDetalhamentoCliente;
import com.aluguelcarros_vrs1.data.cliente.DadosEditarCliente;
import com.aluguelcarros_vrs1.data.cliente.DadosListaCliente;
import com.aluguelcarros_vrs1.services.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cliente")
@SecurityRequirement(name = "bearer-key")
public class ClienteController implements com.aluguelcarros_vrs1.controllers.docs.ClienteControllerDoc {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/cadastrar")
    @Override
    public ResponseEntity<DadosDetalhamentoCliente> cadastrar(@RequestBody @Valid DadosCadastroCliente dados, UriComponentsBuilder uriBuilder) {
        var dto = clienteService.cadastrar(dados);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/listar")
    @Override
    public ResponseEntity<List<DadosListaCliente>> listar() {
        List<DadosListaCliente> clientesAtivos = clienteService.listarAtivos();
        return ResponseEntity.ok(clientesAtivos);
    }

    @PutMapping("/editar/{id}")
    @Override
    public ResponseEntity<DadosDetalhamentoCliente> atualizar(@PathVariable Long id, @RequestBody @Valid DadosEditarCliente dados) {
        var dto = clienteService.atualizar(id, dados);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/desativar/{id}")
    @Override
    public ResponseEntity<DadosDetalhamentoCliente> desativar(@PathVariable Long id) {
        var dto = clienteService.desativar(id);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/ativar/{id}")
    @Override
    public ResponseEntity<DadosDetalhamentoCliente> ativar(@PathVariable Long id) {
        var dto = clienteService.ativar(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @GetMapping("/buscar/{id}")
    @Override
    public ResponseEntity<DadosDetalhamentoCliente> buscar(@PathVariable Long id) {
        var dto = clienteService.buscar(id);
        return ResponseEntity.ok(dto);
    }
}
