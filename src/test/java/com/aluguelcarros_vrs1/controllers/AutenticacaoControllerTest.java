package com.aluguelcarros_vrs1.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aluguelcarros_vrs1.domain.usuario.DadosAutenticacao;
import com.aluguelcarros_vrs1.domain.usuario.Usuario;
import com.aluguelcarros_vrs1.infra.security.TokenService;
import com.aluguelcarros_vrs1.repositories.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AutenticacaoController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AutenticacaoController Tests")
class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    private DadosAutenticacao dadosAutenticacao;

    @BeforeEach
    void setup() {
        dadosAutenticacao = new DadosAutenticacao(
            "user@email.com",
            "senha123"
        );
    }

    @Test
    @DisplayName("Login com credenciais válidas retorna JWT")
    void testGivenValidCredentials_whenLogin_then200() throws Exception {
        // Given
        Usuario usuario = new Usuario("user@email.com", "senha123");
        
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            usuario, 
            null, 
            usuario.getAuthorities()
        );
        
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .willReturn(authentication);
        given(tokenService.gerarToken(usuario))
            .willReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");

        // When & Then
        mockMvc.perform(post("/autenticacao/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dadosAutenticacao)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("Login com credenciais inválidas retorna 401")
    void testGivenInvalidCredentials_whenLogin_then401() throws Exception {
        // Given
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .willThrow(new BadCredentialsException("Credenciais inválidas"));

        // When & Then
        mockMvc.perform(post("/autenticacao/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dadosAutenticacao)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Login com usuário inexistente retorna 401")
    void testGivenNonExistentUser_whenLogin_then401() throws Exception {
        // Given
        DadosAutenticacao dadosInvalidos = new DadosAutenticacao(
            "inexistente@email.com",
            "senha123"
        );
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .willThrow(new BadCredentialsException("Usuário ou senha incorretos"));

        // When & Then
        mockMvc.perform(post("/autenticacao/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dadosInvalidos)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Login com email vazio retorna erro validation")
    void testGivenEmptyEmail_whenLogin_then400() throws Exception {
        // Given
        DadosAutenticacao dadosVazio = new DadosAutenticacao(
            "",
            "senha123"
        );

        // When & Then
        mockMvc.perform(post("/autenticacao/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dadosVazio)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Login com senha vazia retorna erro validation")
    void testGivenEmptyPassword_whenLogin_then400() throws Exception {
        // Given
        DadosAutenticacao dadosVazio = new DadosAutenticacao(
            "user@email.com",
            ""
        );

        // When & Then
        mockMvc.perform(post("/autenticacao/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dadosVazio)))
            .andExpect(status().isBadRequest());
    }
}
