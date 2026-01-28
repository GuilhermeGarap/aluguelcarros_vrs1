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

import com.aluguelcarros_vrs1.domain.carro.DadosCadastroCarro;
import com.aluguelcarros_vrs1.domain.carro.DadosDetalhamentoCarro;
import com.aluguelcarros_vrs1.domain.carro.DadosEditarCarro;
import com.aluguelcarros_vrs1.domain.carro.DadosListaCarro;
import com.aluguelcarros_vrs1.domainservices.carroservices.CarroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/carro")
@SecurityRequirement(name = "bearer-key")
@Tag(name = " Carro", description = "Gerenciamento da frota de carros")
public class CarroController {

    @Autowired
    private CarroService carroService;

    @Operation(summary = "Cadastrar um novo carro", description = "Cria um novo carro no sistema e retorna os detalhes do carro cadastrado.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Carro cadastrado com sucesso", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosDetalhamentoCarro.class)))
    })
    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity<DadosDetalhamentoCarro> cadastrar(@RequestBody @Valid DadosCadastroCarro dados, UriComponentsBuilder uriBuilder) {
        var dto = carroService.cadastrar(dados);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Listar carros", description = "Retorna uma lista de carros ativos.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de carros retornada com sucesso")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<DadosListaCarro>> listar() {
        List<DadosListaCarro> dadosCarros = carroService.listarAtivos();
        return ResponseEntity.ok(dadosCarros);
    }

    

    @Operation(summary = "Atualizar informações de um carro", description = "Edita os dados de um carro existente.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Carro atualizado com sucesso")
    })
    @PutMapping("/editar/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoCarro> atualizar(
        @Parameter(description = "ID do carro a ser atualizado", example = "1")
        @PathVariable Long id, 
        @RequestBody @Valid DadosEditarCarro dados) {
        var dto = carroService.atualizar(id, dados);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Desativar um carro", description = "Desativa um carro específico pelo ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Carro desativado com sucesso")
    })
    @DeleteMapping("/desativar/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoCarro> desativar(@PathVariable Long id) {
        var dto = carroService.desativar(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Ativar um carro", description = "Reativa um carro desativado pelo ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Carro ativado com sucesso"),
        @ApiResponse(responseCode = "304", description = "Carro já está ativo")
    })
    @PatchMapping("/ativar/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoCarro> ativar(@PathVariable Long id) {
        var dto = carroService.ativar(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @Operation(summary = "Buscar um carro por ID", description = "Obtém os detalhes de um carro específico pelo ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detalhes do carro retornados com sucesso")
    })
    @GetMapping("/buscar/{id}")
    public ResponseEntity<DadosDetalhamentoCarro> buscar(@PathVariable Long id) {
        var dto = carroService.buscar(id);
        return ResponseEntity.ok(dto);
    }
}