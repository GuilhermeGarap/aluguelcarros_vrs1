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

import com.aluguelcarros_vrs1.domain.carro.Carro;
import com.aluguelcarros_vrs1.domain.carro.CarroRepository;
import com.aluguelcarros_vrs1.domain.carro.DadosCadastroCarro;
import com.aluguelcarros_vrs1.domain.carro.DadosDetalhamentoCarro;
import com.aluguelcarros_vrs1.domain.carro.DadosEditarCarro;
import com.aluguelcarros_vrs1.domain.carro.DadosListaCarro;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/carro")
public class CarroController {
    
    @Autowired
    private CarroRepository repository;


@PostMapping("/cadastrar")
@Transactional
public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroCarro dados, UriComponentsBuilder uriBuilder) {
    var carro = new Carro(dados);
    repository.save(carro);

    var uri = uriBuilder.path("/carro/cadastrar/{id}").buildAndExpand(carro.getId()).toUri();

    return ResponseEntity.created(uri).body(new DadosDetalhamentoCarro(carro));
}

@GetMapping("/listar")
public ResponseEntity<Page<DadosListaCarro>> listar(@PageableDefault(size=15, sort = {"modelo"}) Pageable paginacao) {
    var page = repository.findAllByAtivoTrue(paginacao).map(DadosListaCarro::new);

    return ResponseEntity.ok(page);
}

@PutMapping("/editar/{id}")
@Transactional
public ResponseEntity atualizar(@PathVariable Long id, @RequestBody DadosEditarCarro dados) {
    var carro = repository.getReferenceById(id);
    carro.atualizarInformacoes(dados);

    return ResponseEntity.ok(new DadosDetalhamentoCarro(carro));
}

@DeleteMapping("/desativar/{id}")
@Transactional
public ResponseEntity desativar(@PathVariable Long id) {
    var carro = repository.getReferenceById(id);
    carro.desativar();

    return ResponseEntity.ok(new DadosDetalhamentoCarro(carro));
}

@PatchMapping("/ativar/{id}")
@Transactional
public ResponseEntity ativar(@PathVariable Long id) {
    var carro = repository.getReferenceById(id);
    if(carro.getAtivo() != true){
        carro.ativar();
        return ResponseEntity.ok(new DadosDetalhamentoCarro(carro));
    } else {
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }
}

@GetMapping("/buscar/{id}")
public ResponseEntity buscar(@PathVariable Long id) {
    var carro = repository.getReferenceById(id);

    return ResponseEntity.ok(new DadosDetalhamentoCarro(carro));
}
}
