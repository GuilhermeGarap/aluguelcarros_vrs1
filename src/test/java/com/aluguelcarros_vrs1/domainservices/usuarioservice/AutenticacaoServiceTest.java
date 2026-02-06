package com.aluguelcarros_vrs1.domainservices.usuarioservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import com.aluguelcarros_vrs1.domain.usuario.Usuario;
import com.aluguelcarros_vrs1.repositories.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AutenticacaoService Tests")
class AutenticacaoServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private AutenticacaoService service;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario("user@email.com", "senha123");
    }

    @Test
    @DisplayName("Carregar usuário por username com sucesso")
    void testGivenValidUsername_whenLoadUserByUsername_thenReturnUserDetails() {
        // Given
        given(repository.findBylogin("user@email.com")).willReturn(usuario);

        // When
        UserDetails resultado = service.loadUserByUsername("user@email.com");

        // Then
        assertNotNull(resultado);
        assertEquals("user@email.com", resultado.getUsername());
        verify(repository).findBylogin("user@email.com");
    }

    @Test
    @DisplayName("Carregar usuário inexistente retorna null")
    void testGivenNonExistentUsername_whenLoadUserByUsername_thenReturnNull() {
        // Given
        given(repository.findBylogin("inexistente@email.com")).willReturn(null);

        // When
        UserDetails resultado = service.loadUserByUsername("inexistente@email.com");

        // Then
        assertEquals(null, resultado);
        verify(repository).findBylogin("inexistente@email.com");
    }
}
