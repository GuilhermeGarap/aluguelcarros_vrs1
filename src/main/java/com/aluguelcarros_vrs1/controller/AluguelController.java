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

import com.aluguelcarros_vrs1.domain.aluguel.Aluguel;
import com.aluguelcarros_vrs1.domain.aluguel.AluguelRepository;
import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.domain.aluguel.DadosDetalhamentoAluguel;
import com.aluguelcarros_vrs1.domain.aluguel.DadosEditarAluguel;
import com.aluguelcarros_vrs1.domain.aluguel.DadosListaAluguel;
import com.aluguelcarros_vrs1.domain.carro.Carro;
import com.aluguelcarros_vrs1.domain.carro.CarroRepository;
import com.aluguelcarros_vrs1.domain.cliente.Cliente;
import com.aluguelcarros_vrs1.domain.cliente.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/aluguel")
public class AluguelController {
    
    @Autowired
    private AluguelRepository repository;

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private ClienteRepository clienteRepository;


    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroAluguel dados, UriComponentsBuilder uriBuilder) {
        Carro carro = carroRepository.findById(dados.carro_id())
            .orElseThrow(() -> new EntityNotFoundException("Carro não encontrado com ID " + dados.carro_id()));
    
        Cliente cliente = clienteRepository.findById(dados.cliente_id())
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID " + dados.cliente_id()));

        if (carro.getDisponivel() <= 0) {
            throw new IllegalStateException("Não há carros disponíveis para este modelo.");
        }
    

        carro.setDisponivel(carro.getDisponivel() - 1);
        carroRepository.save(carro); 
    
        var aluguel = new Aluguel(dados);
        aluguel.setCarro(carro);
        aluguel.setCliente(cliente);
        repository.save(aluguel);
    
        var uri = uriBuilder.path("/aluguel/cadastrar/{id}").buildAndExpand(aluguel.getId()).toUri();
    
        return ResponseEntity.created(uri).body(new DadosDetalhamentoAluguel(aluguel));
    }
    

@GetMapping("/listar")
public ResponseEntity<Page<DadosListaAluguel>> listar(@PageableDefault(size=10) Pageable paginacao) {
    var page = repository.findAllByAtivoTrue(paginacao).map(DadosListaAluguel::new);

    return ResponseEntity.ok(page);
}

@PutMapping("/editar/{id}")
@Transactional
public ResponseEntity atualizar(@PathVariable Long id, @RequestBody DadosEditarAluguel dados) {
    var aluguel = repository.getReferenceById(id);
    aluguel.atualizarInformacoes(dados);

    return ResponseEntity.ok(new DadosDetalhamentoAluguel(aluguel));
}

@DeleteMapping("/desativar/{id}")
@Transactional
public ResponseEntity desativar(@PathVariable Long id) {
    var aluguel = repository.getReferenceById(id);
    aluguel.desativar();

    return ResponseEntity.ok(new DadosDetalhamentoAluguel(aluguel));
}

@PatchMapping("/ativar/{id}")
@Transactional
public ResponseEntity ativar(@PathVariable Long id) {
    var aluguel = repository.getReferenceById(id);
    if(aluguel.getAtivo() != true){
        aluguel.ativar();
        return ResponseEntity.ok(new DadosDetalhamentoAluguel(aluguel));
    } else {
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }
}

@GetMapping("/buscar/{id}")
public ResponseEntity buscar(@PathVariable Long id) {
    var aluguel = repository.getReferenceById(id);

    return ResponseEntity.ok(new DadosDetalhamentoAluguel(aluguel));
}
}