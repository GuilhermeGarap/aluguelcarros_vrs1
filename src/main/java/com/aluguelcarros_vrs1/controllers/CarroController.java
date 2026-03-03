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

import com.aluguelcarros_vrs1.data.carro.DadosCadastroCarro;
import com.aluguelcarros_vrs1.data.carro.DadosDetalhamentoCarro;
import com.aluguelcarros_vrs1.data.carro.DadosEditarCarro;
import com.aluguelcarros_vrs1.data.carro.DadosListaCarro;
import com.aluguelcarros_vrs1.services.CarroService;

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
public class CarroController implements com.aluguelcarros_vrs1.controllers.docs.CarroControllerDoc {

    @Autowired
    private CarroService carroService;

    @PostMapping("/cadastrar")
    @Override
    public ResponseEntity<DadosDetalhamentoCarro> cadastrar(@RequestBody @Valid DadosCadastroCarro dados) {
        var dto = carroService.cadastrar(dados);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/listar")
    @Override
    public ResponseEntity<List<DadosListaCarro>> listar() {
        List<DadosListaCarro> dadosCarros = carroService.listarAtivos();
        return ResponseEntity.ok(dadosCarros);
    }

    @PutMapping("/editar/{id}")
    @Override
    public ResponseEntity<DadosDetalhamentoCarro> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosEditarCarro dados) {
        var dto = carroService.atualizar(id, dados);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/desativar/{id}")
    @Override
    public ResponseEntity<DadosDetalhamentoCarro> desativar(@PathVariable Long id) {
        var dto = carroService.desativar(id);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/ativar/{id}")
    @Override
    public ResponseEntity<DadosDetalhamentoCarro> ativar(@PathVariable Long id) {
        var dto = carroService.ativar(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @GetMapping("/buscar/{id}")
    @Override
    public ResponseEntity<DadosDetalhamentoCarro> buscar(@PathVariable Long id) {
        var dto = carroService.buscar(id);
        return ResponseEntity.ok(dto);
    }
}