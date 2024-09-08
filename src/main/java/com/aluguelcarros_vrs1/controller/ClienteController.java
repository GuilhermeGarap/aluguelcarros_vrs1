
package com.aluguelcarros_vrs1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository repository;

    /**
     * Endpoint para cadastrar um novo cliente.
     * @param dados Dados do cliente a ser cadastrado.
     * @param uriBuilder Utilizado para construir a URI do novo recurso.
     * @return Resposta com os detalhes do cliente cadastrado e a URI do recurso.
     */
    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity<DadosDetalhamentoCliente> cadastrar(@RequestBody @Valid DadosCadastroCliente dados, UriComponentsBuilder uriBuilder) {
        var cliente = new Cliente(dados);
        cliente.verificadorCpf(cliente.getCpf());
        repository.save(cliente);

        var uri = uriBuilder.path("/cliente/cadastrar/{id}").buildAndExpand(cliente.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoCliente(cliente));
    }

    /**
     * Endpoint para listar todos os clientes ativos com paginação.
     * @param paginacao Configurações de paginação.
     * @return Resposta com a lista paginada de clientes ativos.
     */
    @GetMapping("/listar")
    public ResponseEntity<Page<DadosListaCliente>> listar(@PageableDefault(size=15, sort = {"nome"}) Pageable paginacao) {
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListaCliente::new);
        return ResponseEntity.ok(page);
    }

    /**
     * Endpoint para atualizar as informações de um cliente existente.
     * @param id ID do cliente a ser atualizado.
     * @param dados Novos dados para atualizar o cliente.
     * @return Resposta com os detalhes atualizados do cliente.
     */
    @PutMapping("/editar/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoCliente> atualizar(@PathVariable Long id, @RequestBody @Valid DadosEditarCliente dados) {
        var cliente = repository.getReferenceById(id);
        cliente.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoCliente(cliente));
    }

    /**
     * Endpoint para desativar um cliente.
     * @param id ID do cliente a ser desativado.
     * @return Resposta com os detalhes do cliente desativado.
     */
    @DeleteMapping("/desativar/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoCliente> desativar(@PathVariable Long id) {
        var cliente = repository.getReferenceById(id);
        cliente.desativar();
        repository.save(cliente); // Salvando o cliente desativado

        return ResponseEntity.ok(new DadosDetalhamentoCliente(cliente));
    }

    /**
     * Endpoint para ativar um cliente.
     * @param id ID do cliente a ser ativado.
     * @return Resposta com os detalhes do cliente ativado, ou um status 304 se o cliente já estiver ativo.
     */
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

    /**
     * Endpoint para buscar um cliente pelo ID.
     * @param id ID do cliente a ser buscado.
     * @return Resposta com os detalhes do cliente encontrado.
     */
    @GetMapping("/buscar/{id}")
    public ResponseEntity<DadosDetalhamentoCliente> buscar(@PathVariable Long id) {
        var cliente = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoCliente(cliente));
    }
}