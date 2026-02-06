package com.aluguelcarros_vrs1.domainservices.aluguelservices;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.infra.exception.ValidacaoException;
import com.aluguelcarros_vrs1.repositories.ClienteRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AluguelValidarClienteAtivo Tests")
class AluguelValidarClienteAtivoTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private AluguelValidarClienteAtivo validador;

    private DadosCadastroAluguel dados;

    @BeforeEach
    void setup() {
        dados = new DadosCadastroAluguel(
            LocalDate.of(2026, 2, 2),
            LocalDate.of(2026, 2, 6),
            1L,
            1L
        );
    }

    @Test
    @DisplayName("Rejeita aluguel com cliente inativo")
    void testGivenInactiveCliente_whenValidar_thenThrowValidacaoException() {
        // Given
        given(clienteRepository.findAtivoById(1L)).willReturn(false);

        // When & Then
        var exception = assertThrows(ValidacaoException.class, () -> {
            validador.validar(dados);
        });
        assertEquals("Esse cliente está desativado no sistema", exception.getMessage());
    }

    @Test
    @DisplayName("Aceita aluguel com cliente ativo")
    void testGivenActiveCliente_whenValidar_thenNoException() {
        // Given
        given(clienteRepository.findAtivoById(1L)).willReturn(true);

        // When & Then
        assertDoesNotThrow(() -> {
            validador.validar(dados);
        });
    }
}
