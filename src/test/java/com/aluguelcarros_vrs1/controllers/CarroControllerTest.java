package com.aluguelcarros_vrs1.controllers;

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

import com.aluguelcarros_vrs1.domain.carro.DadosCadastroCarro;
import com.aluguelcarros_vrs1.domain.carro.DadosDetalhamentoCarro;
import com.aluguelcarros_vrs1.domain.carro.DadosEditarCarro;
import com.aluguelcarros_vrs1.domain.carro.DadosListaCarro;
import com.aluguelcarros_vrs1.domainservices.carroservices.CarroService;
import com.aluguelcarros_vrs1.infra.exception.ValidacaoException;
import com.aluguelcarros_vrs1.infra.security.TokenService;
import com.aluguelcarros_vrs1.repositories.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CarroController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("CarroController Tests")
class CarroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarroService carroService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    private DadosCadastroCarro dadosCadastro;
    private DadosDetalhamentoCarro dadosDetalhamento;
    private DadosListaCarro dadosLista;

    @BeforeEach
    void setup() {
        dadosCadastro = new DadosCadastroCarro(
            "Fiat Uno",
            15F,
            5
        );

        dadosDetalhamento = new DadosDetalhamentoCarro(
            1L,
            "Fiat Uno",
            15F,
            5,
            3,
            true
        );

        dadosLista = new DadosListaCarro(
            1L,
            "Fiat Uno",
            15F,
            5,
            3,
            true
        );
    }

    @Test
    @DisplayName("Cadastro de carro com sucesso")
    void testGivenValidDadosCadastroCarro_whenCadastrar_then200() throws Exception {
        // Given
        given(carroService.cadastrar(any(DadosCadastroCarro.class))).willReturn(dadosDetalhamento);

        // When & Then
        mockMvc.perform(post("/carro/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dadosCadastro)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Listar carros ativos com sucesso")
    void testGivenActiveCarroList_whenListar_then200() throws Exception {
        // Given
        List<DadosListaCarro> listaCarros = List.of(dadosLista);
        given(carroService.listarAtivos()).willReturn(listaCarros);

        // When & Then
        mockMvc.perform(get("/carro/listar")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].modelo").value("Fiat Uno"));
    }

    @Test
    @DisplayName("Buscar carro por ID com sucesso")
    void testGivenValidCarroId_whenBuscar_then200() throws Exception {
        // Given
        given(carroService.buscar(1L)).willReturn(dadosDetalhamento);

        // When & Then
        mockMvc.perform(get("/carro/buscar/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Buscar carro por ID inválido retorna erro")
    void testGivenInvalidCarroId_whenBuscar_then404() throws Exception {
        // Given
        given(carroService.buscar(999L))
            .willThrow(new ValidacaoException("Não existe um carro com esse ID"));

        // When & Then
        mockMvc.perform(get("/carro/buscar/999")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Desativar carro com sucesso")
    void testGivenValidCarroId_whenDesativar_then200() throws Exception {
        // Given
        DadosDetalhamentoCarro dadosInativo = new DadosDetalhamentoCarro(
            1L,
            "Fiat Uno",
            15F,
            5,
            3,
            false
        );
        given(carroService.desativar(1L)).willReturn(dadosInativo);

        // When & Then
        mockMvc.perform(delete("/carro/desativar/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ativo").value(false));
    }

    @Test
    @DisplayName("Ativar carro com sucesso")
    void testGivenInactiveCarroId_whenAtivar_then200() throws Exception {
        // Given
        given(carroService.ativar(1L)).willReturn(dadosDetalhamento);

        // When & Then
        mockMvc.perform(patch("/carro/ativar/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Atualizar carro com sucesso")
    void testGivenValidDadosEditar_whenAtualizar_then200() throws Exception {
        // Given
        DadosEditarCarro dadosEditar = new DadosEditarCarro(
            "Fiat Uno Updated",
            17F,
            4
        );
        given(carroService.atualizar(anyLong(), any(DadosEditarCarro.class)))
            .willReturn(dadosDetalhamento);

        // When & Then
        mockMvc.perform(put("/carro/editar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dadosEditar)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Listar carros ativos retorna lista vazia")
    void testGivenEmptyCarroList_whenListar_then200() throws Exception {
        // Given
        given(carroService.listarAtivos()).willReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/carro/listar")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(0)));
    }

    @Test
    @DisplayName("Cadastro falha com modelo duplicado")
    void testGivenDuplicateModelo_whenCadastrar_then400() throws Exception {
        // Given
        given(carroService.cadastrar(any(DadosCadastroCarro.class)))
            .willThrow(new ValidacaoException("Esse nome de modelo de carro já está registrado!"));

        // When & Then
        mockMvc.perform(post("/carro/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dadosCadastro)))
            .andExpect(status().isBadRequest());
    }
}
