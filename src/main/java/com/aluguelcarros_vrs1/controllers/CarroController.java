package com.aluguelcarros_vrs1.controllers;

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

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/carro")
@SecurityRequirement(name = "bearer-key")
public class CarroController {

    @Autowired
    private CarroRepository repository;

    /**
     * Endpoint para cadastrar um novo carro.
     * @param dados Dados do carro a ser cadastrado.
     * @param uriBuilder Utilizado para construir a URI do novo recurso.
     * @return Resposta com os detalhes do carro cadastrado e a URI do recurso.
     */
    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity<DadosDetalhamentoCarro> cadastrar(@RequestBody @Valid DadosCadastroCarro dados, UriComponentsBuilder uriBuilder) {
        var carro = new Carro(dados);
        repository.save(carro);

        var uri = uriBuilder.path("/carro/cadastrar/{id}").buildAndExpand(carro.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoCarro(carro));
    }

    /**
     * Endpoint para listar todos os carros ativos com paginação.
     * @param paginacao Configurações de paginação.
     * @return Resposta com a lista paginada de carros ativos.
     */
    @GetMapping("/listar")
    public ResponseEntity<Page<DadosListaCarro>> listar(@PageableDefault(size=15, sort = {"modelo"}) Pageable paginacao) {
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListaCarro::new);

        return ResponseEntity.ok(page);
    }

    /**
     * Endpoint para atualizar as informações de um carro existente.
     * @param id ID do carro a ser atualizado.
     * @param dados Novos dados para atualizar o carro.
     * @return Resposta com os detalhes atualizados do carro.
     */
    @PutMapping("/editar/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoCarro> atualizar(@PathVariable Long id, @RequestBody @Valid DadosEditarCarro dados) {
        var carro = repository.getReferenceById(id);
        carro.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoCarro(carro));
    }

    /**
     * Endpoint para desativar um carro.
     * @param id ID do carro a ser desativado.
     * @return Resposta com os detalhes do carro desativado.
     */
    @DeleteMapping("/desativar/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoCarro> desativar(@PathVariable Long id) {
        var carro = repository.getReferenceById(id);
        carro.desativar();
        repository.save(carro); // Salvando o carro desativado

        return ResponseEntity.ok(new DadosDetalhamentoCarro(carro));
    }

    /**
     * Endpoint para ativar um carro.
     * @param id ID do carro a ser ativado.
     * @return Resposta com os detalhes do carro ativado, ou um status 304 se o carro já estiver ativo.
     */
    @PatchMapping("/ativar/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoCarro> ativar(@PathVariable Long id) {
        var carro = repository.getReferenceById(id);
        if (!carro.getAtivo()) {
            carro.ativar();
            repository.save(carro); // Salvando o carro ativado
            return ResponseEntity.ok(new DadosDetalhamentoCarro(carro));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    /**
     * Endpoint para buscar um carro pelo ID.
     * @param id ID do carro a ser buscado.
     * @return Resposta com os detalhes do carro encontrado.
     */
    @GetMapping("/buscar/{id}")
    public ResponseEntity<DadosDetalhamentoCarro> buscar(@PathVariable Long id) {
        var carro = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoCarro(carro));
    }
}
