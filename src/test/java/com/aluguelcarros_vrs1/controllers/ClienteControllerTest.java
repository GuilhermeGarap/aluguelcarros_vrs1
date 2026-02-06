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

import com.aluguelcarros_vrs1.domain.cliente.DadosCadastroCliente;
import com.aluguelcarros_vrs1.domain.cliente.DadosDetalhamentoCliente;
import com.aluguelcarros_vrs1.domain.cliente.DadosEditarCliente;
import com.aluguelcarros_vrs1.domain.cliente.DadosListaCliente;
import com.aluguelcarros_vrs1.domain.endereco.DadosEndereco;
import com.aluguelcarros_vrs1.domain.endereco.Endereco;
import com.aluguelcarros_vrs1.domainservices.clienteservices.ClienteService;
import com.aluguelcarros_vrs1.infra.exception.ValidacaoException;
import com.aluguelcarros_vrs1.infra.security.TokenService;
import com.aluguelcarros_vrs1.repositories.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ClienteController Tests")
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    private DadosCadastroCliente dadosCadastro;
    private DadosDetalhamentoCliente dadosDetalhamento;
    private DadosListaCliente dadosLista;
    private DadosEndereco dadosEndereco;
    private Endereco endereco;

    @BeforeEach
    void setup() {
        dadosEndereco = new DadosEndereco("Avenida Paulista", "1000", "Centro", "Apto 101", "01310-100", "São Paulo", "SP");
        endereco = new Endereco(dadosEndereco);
        dadosCadastro = new DadosCadastroCliente(
            "João Silva",
            "joao@email.com",
            "11987654321",
            "529.982.247-25",
            LocalDate.of(2003, 5, 1),
            dadosEndereco
        );

        dadosDetalhamento = new DadosDetalhamentoCliente(
            1L,
            "João Silva",
            "joao@email.com",
            "11987654321",
            "529.982.247-25",
            LocalDate.of(2003, 5, 1),
            endereco,
            true
        );

        dadosLista = new DadosListaCliente(
            1L,
            "João Silva",
            "joao@email.com",
            "11987654321",
            "529.982.247-25"
        );
    }

    @Test
    @DisplayName("Cadastro de cliente com sucesso")
    void testGivenValidDadosCadastroCliente_whenCadastrar_then200() throws Exception {
        // Given
        given(clienteService.cadastrar(any(DadosCadastroCliente.class))).willReturn(dadosDetalhamento);

        // When & Then
        mockMvc.perform(post("/cliente/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dadosCadastro)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Listar clientes ativos com sucesso")
    void testGivenActiveClienteList_whenListar_then200() throws Exception {
        // Given
        List<DadosListaCliente> listaClientes = List.of(dadosLista);
        given(clienteService.listarAtivos()).willReturn(listaClientes);

        // When & Then
        mockMvc.perform(get("/cliente/listar")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("Buscar cliente por ID com sucesso")
    void testGivenValidClienteId_whenBuscar_then200() throws Exception {
        // Given
        given(clienteService.buscar(1L)).willReturn(dadosDetalhamento);

        // When & Then
        mockMvc.perform(get("/cliente/buscar/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Buscar cliente por ID inválido retorna erro")
    void testGivenInvalidClienteId_whenBuscar_then404() throws Exception {
        // Given
        given(clienteService.buscar(999L))
            .willThrow(new ValidacaoException("Cliente não encontrado com o ID: 999"));

        // When & Then
        mockMvc.perform(get("/cliente/buscar/999")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Desativar cliente com sucesso")
    void testGivenValidClienteId_whenDesativar_then200() throws Exception {
        // Given
        DadosDetalhamentoCliente dadosInativo = new DadosDetalhamentoCliente(
            1L,
            "João Silva",
            "joao@email.com",
            "11987654321",
            "123.456.789-00",
            LocalDate.of(2003, 5, 1),
            endereco,
            false
        );
        given(clienteService.desativar(1L)).willReturn(dadosInativo);

        // When & Then
        mockMvc.perform(delete("/cliente/desativar/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ativo").value(false));
    }

    @Test
    @DisplayName("Ativar cliente com sucesso")
    void testGivenInactiveClienteId_whenAtivar_then200() throws Exception {
        // Given
        given(clienteService.ativar(1L)).willReturn(dadosDetalhamento);

        // When & Then
        mockMvc.perform(patch("/cliente/ativar/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Atualizar cliente com sucesso")
    void testGivenValidDadosEditar_whenAtualizar_then200() throws Exception {
        // Given
        DadosEditarCliente dadosEditar = new DadosEditarCliente(
            "João Silva Updated",
            "joao_updated@email.com",
            "11987654322",
            "529.982.247-25",
            LocalDate.of(2003, 5, 1),
            dadosEndereco
        );
        given(clienteService.atualizar(anyLong(), any(DadosEditarCliente.class)))
            .willReturn(dadosDetalhamento);

        // When & Then
        mockMvc.perform(put("/cliente/editar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dadosEditar)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Listar clientes ativos retorna lista vazia")
    void testGivenEmptyClienteList_whenListar_then200() throws Exception {
        // Given
        given(clienteService.listarAtivos()).willReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/cliente/listar")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(0)));
    }

    @Test
    @DisplayName("Cadastro falha com CPF duplicado")
    void testGivenDuplicateCPF_whenCadastrar_then400() throws Exception {
        // Given
        given(clienteService.cadastrar(any(DadosCadastroCliente.class)))
            .willThrow(new ValidacaoException("Já existe um cliente cadastrado com esse CPF!"));

        // When & Then
        mockMvc.perform(post("/cliente/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dadosCadastro)))
            .andExpect(status().isBadRequest());
    }
}
