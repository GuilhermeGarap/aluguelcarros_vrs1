package com.aluguelcarros_vrs1.controllers.docs;

import com.aluguelcarros_vrs1.data.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.data.aluguel.DadosDetalhamentoAluguel;
import com.aluguelcarros_vrs1.data.aluguel.DadosEditarAluguel;
import com.aluguelcarros_vrs1.data.aluguel.DadosListaAluguel;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AluguelControllerDoc {
    @Operation(summary = "Cadastra um novo aluguel", description = "Endpoint para registrar um novo aluguel de carro")
    ResponseEntity<DadosDetalhamentoAluguel> cadastrar(DadosCadastroAluguel dados);

    @Operation(summary = "Lista todos os aluguéis ativos", description = "Retorna uma lista de aluguéis ativos")
    ResponseEntity<List<DadosListaAluguel>> listarAtivos();

    @Operation(summary = "Lista todos os aluguéis desativados", description = "Retorna uma lista de aluguéis desativados")
    ResponseEntity<List<DadosListaAluguel>> listarDesativados();

    @Operation(summary = "Lista todos os aluguéis", description = "Retorna uma lista de todos os aluguéis")
    ResponseEntity<List<DadosListaAluguel>> listarTodos();

    @Operation(summary = "Atualiza um aluguel", description = "Atualiza as informações de um aluguel existente")
    ResponseEntity<DadosDetalhamentoAluguel> atualizar(Long id, DadosEditarAluguel dados);

    @Operation(summary = "Busca um aluguel pelo ID", description = "Retorna um aluguel com base no ID fornecido")
    ResponseEntity<DadosDetalhamentoAluguel> buscar(Long id);
}
