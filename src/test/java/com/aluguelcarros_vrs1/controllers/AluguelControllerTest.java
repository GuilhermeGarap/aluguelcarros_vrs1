package com.aluguelcarros_vrs1.controllers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.domain.aluguel.DadosDetalhamentoAluguel;
import com.aluguelcarros_vrs1.domain.aluguel.DadosEditarAluguel;
import com.aluguelcarros_vrs1.domain.aluguel.DadosListaAluguel;
import com.aluguelcarros_vrs1.domain.carro.Carro;
import com.aluguelcarros_vrs1.domain.carro.DadosListaCarro;
import com.aluguelcarros_vrs1.domain.cliente.Cliente;
import com.aluguelcarros_vrs1.domain.cliente.DadosListaCliente;
import com.aluguelcarros_vrs1.domain.endereco.Endereco;
import com.aluguelcarros_vrs1.domainservices.aluguelservices.AluguelService;
import com.aluguelcarros_vrs1.infra.exception.ValidacaoException;
import com.aluguelcarros_vrs1.infra.security.TokenService;
import com.aluguelcarros_vrs1.repositories.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AluguelController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AluguelController Tests")
class AluguelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AluguelService aluguelService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    private DadosCadastroAluguel dadosCadastro;
    private DadosDetalhamentoAluguel dadosDetalhamento;
    private DadosListaAluguel dadosLista;

    @BeforeEach
    void setup() {
        dadosCadastro = new DadosCadastroAluguel(
            LocalDate.of(2026, 2, 10),
            LocalDate.of(2026, 2, 14),
            1L,
            1L
        );

        dadosDetalhamento = new DadosDetalhamentoAluguel(
            1L,
            LocalDate.of(2026, 2, 10),
            LocalDate.of(2026, 2, 14),
            true,
            1L,
            "123.456.789-00",
            "João Silva",
            1L,
            "Fiat Uno"
        );

        dadosLista = new DadosListaAluguel(
            1L,
            LocalDate.of(2026, 2, 10),
            LocalDate.of(2026, 2, 14),
            true,
            new DadosListaCarro(1L, "Fiat Uno", 15F, 5, 3, true),
            new DadosListaCliente(1L, "João Silva", "joao@email.com", "11987654321", "123.456.789-00")
        );
    }

    @Test
    @DisplayName("Cadastro de aluguel com sucesso")
    void testGivenValidDadosCadastroAluguel_whenCadastrar_then200() throws Exception {
        // Given
        given(aluguelService.cadastrar(any(DadosCadastroAluguel.class))).willReturn(dadosDetalhamento);

        // When & Then
        mockMvc.perform(post("/aluguel/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dadosCadastro)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Listar aluguéis ativos com sucesso")
    void testGivenActiveAluguelList_whenListarAtivos_then200() throws Exception {
        // Given
        List<DadosListaAluguel> listaAlugueis = List.of(dadosLista);
        given(aluguelService.listarAtivos()).willReturn(listaAlugueis);

        // When & Then
        mockMvc.perform(get("/aluguel/listarAtivos")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].ativo").value(true));
    }

    @Test
    @DisplayName("Listar aluguéis desativados com sucesso")
    void testGivenInactiveAluguelList_whenListarDesativados_then200() throws Exception {
        // Given
        DadosListaAluguel dadosListaInativo = new DadosListaAluguel(
            1L,
            LocalDate.of(2026, 2, 10),
            LocalDate.of(2026, 2, 14),
            false,
            new DadosListaCarro(1L, "Fiat Uno", 15F, 5, 3, true),
            new DadosListaCliente(1L, "João Silva", "joao@email.com", "11987654321", "123.456.789-00")
        );
        List<DadosListaAluguel> listaAlugueis = List.of(dadosListaInativo);
        given(aluguelService.listarDesativados()).willReturn(listaAlugueis);

        // When & Then
        mockMvc.perform(get("/aluguel/listarDesativados")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].ativo").value(false));
    }

    @Test
    @DisplayName("Listar todos os aluguéis com sucesso")
    void testGivenAluguelList_whenListarTodos_then200() throws Exception {
        // Given
        List<DadosListaAluguel> listaAlugueis = List.of(dadosLista);
        given(aluguelService.listarTodos()).willReturn(listaAlugueis);

        // When & Then
        mockMvc.perform(get("/aluguel/listarTodos")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].ativo").value(true));
    }

    @Test
    @DisplayName("Buscar aluguel por ID com sucesso")
    void testGivenValidAluguelId_whenBuscar_then200() throws Exception {
        // Given
        given(aluguelService.buscar(1L)).willReturn(dadosDetalhamento);

        // When & Then
        mockMvc.perform(get("/aluguel/buscar/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Buscar aluguel por ID inválido retorna erro")
    void testGivenInvalidAluguelId_whenBuscar_then404() throws Exception {
        // Given
        given(aluguelService.buscar(999L))
            .willThrow(new ValidacaoException("Não existe um aluguel com esse ID"));

        // When & Then
        mockMvc.perform(get("/aluguel/buscar/999")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Desativar aluguel com sucesso")
    void testGivenValidAluguelId_whenDesativar_then200() throws Exception {
        // Given
        DadosDetalhamentoAluguel dadosInativo = new DadosDetalhamentoAluguel(
            1L,
            LocalDate.of(2026, 2, 10),
            LocalDate.of(2026, 2, 14),
            false,
            1L,
            "123.456.789-00",
            "João Silva",
            1L,
            "Fiat Uno"
        );
        given(aluguelService.desativarAluguel(1L)).willReturn(dadosInativo);

        // When & Then
        mockMvc.perform(delete("/aluguel/desativar/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ativo").value(false));
    }

    @Test
    @DisplayName("Ativar aluguel com sucesso")
    void testGivenInactiveAluguelId_whenAtivar_then200() throws Exception {
        // Given
        given(aluguelService.ativar(1L)).willReturn(dadosDetalhamento);

        // When & Then
        mockMvc.perform(patch("/aluguel/ativar/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Atualizar aluguel com sucesso")
    void testGivenValidDadosEditar_whenAtualizar_then200() throws Exception {
        // Given
        DadosEditarAluguel dadosEditar = new DadosEditarAluguel(
            LocalDate.of(2026, 2, 10),
            LocalDate.of(2026, 2, 15)
        );
        Endereco endereco = new Endereco("Centro", "01310-100", "Sao Paulo", "Apto 101", "Avenida Paulista", "1000", "SP");
        Cliente cliente = new Cliente("Joao Silva", "11987654321", "joao@email.com", "529.982.247-25", LocalDate.of(2003, 5, 1), endereco, true);
        cliente.setId(1L);
        Carro carro = new Carro("Fiat Uno", 15F, 5, true, 3);
        carro.setId(1L);
        var aluguel = new com.aluguelcarros_vrs1.domain.aluguel.Aluguel(LocalDate.of(2026, 2, 10), LocalDate.of(2026, 2, 15), true, cliente, carro);
        aluguel.setId(1L);
        given(aluguelService.atualizar(anyLong(), any(DadosEditarAluguel.class)))
            .willReturn(aluguel);

        // When & Then
        mockMvc.perform(put("/aluguel/editar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dadosEditar)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Listar aluguéis ativos retorna lista vazia")
    void testGivenEmptyAluguelList_whenListarAtivos_then200() throws Exception {
        // Given
        given(aluguelService.listarAtivos()).willReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/aluguel/listarAtivos")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(0)));
    }
}
