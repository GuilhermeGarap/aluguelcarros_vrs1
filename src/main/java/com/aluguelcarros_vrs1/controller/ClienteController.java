
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

@PostMapping("/cadastrar")
@Transactional
public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroCliente dados, UriComponentsBuilder uriBuilder) {
    var cliente = new Cliente(dados);
    cliente.verificadorCpf(cliente.getCpf());
    repository.save(cliente);

    var uri = uriBuilder.path("/cliente/cadastrar/{id}").buildAndExpand(cliente.getId()).toUri();

    return ResponseEntity.created(uri).body(new DadosDetalhamentoCliente(cliente));
    }

@GetMapping("/listar")
public ResponseEntity<Page<DadosListaCliente>> listar(@PageableDefault(size=15, sort = {"nome"}) Pageable paginacao) {
    var page = repository.findAllByAtivoTrue(paginacao).map(DadosListaCliente::new);
    return ResponseEntity.ok(page);
}

@PutMapping("/editar/{id}")
@Transactional
public ResponseEntity atualizar(@PathVariable Long id, @RequestBody DadosEditarCliente dados) {
    var cliente = repository.getReferenceById(id);
    cliente.atualizarInformacoes(dados);

    return ResponseEntity.ok(new DadosDetalhamentoCliente(cliente));
}

@DeleteMapping("/desativar/{id}")
@Transactional
public ResponseEntity desativar(@PathVariable Long id) {
    var cliente = repository.getReferenceById(id);
    cliente.desativar();

    return ResponseEntity.ok(new DadosDetalhamentoCliente(cliente));
}

@PatchMapping("/ativar/{id}")
@Transactional
public ResponseEntity ativar(@PathVariable Long id) {
    var cliente = repository.getReferenceById(id);
    if(cliente.getAtivo() != true){
        cliente.ativar();
        return ResponseEntity.ok(new DadosDetalhamentoCliente(cliente));
    } else {
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }
    
}

@GetMapping("/buscar/{id}")
public ResponseEntity buscar(@PathVariable Long id) {
    var cliente = repository.getReferenceById(id);

    return ResponseEntity.ok(new DadosDetalhamentoCliente(cliente));
}

}
