package com.aluguelcarros_vrs1.controllers.docs;

import com.aluguelcarros_vrs1.data.carro.DadosCadastroCarro;
import com.aluguelcarros_vrs1.data.carro.DadosDetalhamentoCarro;
import com.aluguelcarros_vrs1.data.carro.DadosEditarCarro;
import com.aluguelcarros_vrs1.data.carro.DadosListaCarro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface CarroControllerDoc {
    @Operation(summary = "Cadastrar um novo carro", description = "Cria um novo carro no sistema e retorna os detalhes do carro cadastrado.")
    ResponseEntity<DadosDetalhamentoCarro> cadastrar(DadosCadastroCarro dados);

    @Operation(summary = "Listar carros", description = "Retorna uma lista de carros ativos.")
    ResponseEntity<List<DadosListaCarro>> listar();

    @Operation(summary = "Atualizar informações de um carro", description = "Edita os dados de um carro existente.")
    ResponseEntity<DadosDetalhamentoCarro> atualizar(
            Long id,
            DadosEditarCarro dados);

    @Operation(summary = "Desativar um carro", description = "Desativa um carro específico pelo ID.")
    ResponseEntity<DadosDetalhamentoCarro> desativar(Long id);

    @Operation(summary = "Ativar um carro", description = "Reativa um carro desativado pelo ID.")
    ResponseEntity<DadosDetalhamentoCarro> ativar(Long id);

    @Operation(summary = "Buscar um carro por ID", description = "Obtém os detalhes de um carro específico pelo ID.")
    ResponseEntity<DadosDetalhamentoCarro> buscar(Long id);
}
