package com.aluguelcarros_vrs1.domainservices.aluguelservices;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

import com.aluguelcarros_vrs1.domain.aluguel.Aluguel;
import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.domain.cliente.Cliente;
import com.aluguelcarros_vrs1.domain.endereco.Endereco;
import com.aluguelcarros_vrs1.infra.exception.ValidacaoException;
import com.aluguelcarros_vrs1.repositories.ClienteRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("ValidarSeClienteTemAluguelAtivo Tests")
class ValidarSeClienteTemAluguelAtivoTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ValidarSeClienteTemAluguelAtivo validador;

    private DadosCadastroAluguel dados;
    private Cliente clienteComAluguelAtivo;
    private Cliente clienteSemAluguelAtivo;

    @BeforeEach
    void setup() {
        dados = new DadosCadastroAluguel(
            LocalDate.of(2026, 2, 2),
            LocalDate.of(2026, 2, 6),
            1L,
            1L
        );

        Endereco endereco = new Endereco("Avenida Paulista", "1000", "Centro", "Apto 101", "01310-100", "São Paulo", "SP");
        clienteComAluguelAtivo = new Cliente("João Silva", "joao@email.com", "11987654321", "123.456.789-00", LocalDate.of(2003, 5, 1), endereco, true);
        clienteSemAluguelAtivo = new Cliente("Maria Santos", "maria@email.com", "11987654322", "123.456.789-01", LocalDate.of(2003, 5, 1), endereco, true);

        // Simular que clienteComAluguelAtivo tem um aluguel ativo
        Aluguel aluguelAtivo = new Aluguel(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 5), true, clienteComAluguelAtivo, null);
        clienteComAluguelAtivo.setAlugueis(List.of(aluguelAtivo));
        clienteSemAluguelAtivo.setAlugueis(Collections.emptyList());
    }

    @Test
    @DisplayName("Rejeita aluguel se cliente tem aluguel ativo")
    void testGivenClienteWithActiveAluguel_whenValidar_thenThrowValidacaoException() {
        // Given
        given(clienteRepository.findById(1L)).willReturn(Optional.of(clienteComAluguelAtivo));

        // When & Then
        var exception = assertThrows(ValidacaoException.class, () -> {
            validador.validar(dados);
        });
        assertEquals("Este cliente já possui um aluguel ativo. Finalize o aluguel anterior antes de criar um novo.", exception.getMessage());
    }

    @Test
    @DisplayName("Aceita aluguel se cliente não tem aluguel ativo")
    void testGivenClienteWithoutActiveAluguel_whenValidar_thenNoException() {
        // Given
        given(clienteRepository.findById(1L)).willReturn(Optional.of(clienteSemAluguelAtivo));

        // When & Then
        assertDoesNotThrow(() -> {
            validador.validar(dados);
        });
    }

    @Test
    @DisplayName("Aceita aluguel se cliente tinha aluguel mas foi desativado")
    void testGivenClienteWithInactiveAluguel_whenValidar_thenNoException() {
        // Given
        Aluguel aluguelInativo = new Aluguel(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 5), false, clienteSemAluguelAtivo, null);
        clienteSemAluguelAtivo.setAlugueis(List.of(aluguelInativo));
        given(clienteRepository.findById(1L)).willReturn(Optional.of(clienteSemAluguelAtivo));

        // When & Then
        assertDoesNotThrow(() -> {
            validador.validar(dados);
        });
    }
}
