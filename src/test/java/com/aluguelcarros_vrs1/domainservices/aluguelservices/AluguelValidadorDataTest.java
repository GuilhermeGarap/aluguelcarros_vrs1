package com.aluguelcarros_vrs1.domainservices.aluguelservices;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.infra.exception.ValidacaoException;

@DisplayName("AluguelValidadorData Tests")
class AluguelValidadorDataTest {

    private AluguelValidadorData validador;

    @BeforeEach
    void setup() {
        validador = new AluguelValidadorData();
    }

    @Test
    @DisplayName("Rejeita aluguel começando no domingo")
    void testGivenDataInicioSunday_whenValidar_thenThrowValidacaoException() {
        // Given - Domingo, 2 de fevereiro de 2026
        DadosCadastroAluguel dados = new DadosCadastroAluguel(
            LocalDate.of(2026, 2, 1),  // Domingo
            LocalDate.of(2026, 2, 5),  // Quinta
            1L,
            1L
        );

        // When & Then
        var exception = assertThrows(ValidacaoException.class, () -> {
            validador.validar(dados);
        });
        assertEquals("Data de Início ou Término não pode ser no domingo", exception.getMessage());
    }

    @Test
    @DisplayName("Rejeita aluguel terminando no domingo")
    void testGivenDataTerminoSunday_whenValidar_thenThrowValidacaoException() {
        // Given - Segunda a domingo
        DadosCadastroAluguel dados = new DadosCadastroAluguel(
            LocalDate.of(2026, 2, 2),  // Segunda
            LocalDate.of(2026, 2, 8),  // Domingo
            1L,
            1L
        );

        // When & Then
        var exception = assertThrows(ValidacaoException.class, () -> {
            validador.validar(dados);
        });
        assertEquals("Data de Início ou Término não pode ser no domingo", exception.getMessage());
    }

    @Test
    @DisplayName("Rejeita aluguel com ambas datas no domingo")
    void testGivenBothDatesSunday_whenValidar_thenThrowValidacaoException() {
        // Given
        DadosCadastroAluguel dados = new DadosCadastroAluguel(
            LocalDate.of(2026, 2, 1),  // Domingo
            LocalDate.of(2026, 2, 8),  // Domingo
            1L,
            1L
        );

        // When & Then
        var exception = assertThrows(ValidacaoException.class, () -> {
            validador.validar(dados);
        });
        assertEquals("Data de Início ou Término não pode ser no domingo", exception.getMessage());
    }

    @Test
    @DisplayName("Aceita aluguel em dias úteis")
    void testGivenValidWeekDays_whenValidar_thenNoException() {
        // Given - Segunda a quinta
        DadosCadastroAluguel dados = new DadosCadastroAluguel(
            LocalDate.of(2026, 2, 2),  // Segunda
            LocalDate.of(2026, 2, 5),  // Quinta
            1L,
            1L
        );

        // When & Then
        assertDoesNotThrow(() -> {
            validador.validar(dados);
        });
    }

    @Test
    @DisplayName("Aceita aluguel que inicia segunda e termina sexta")
    void testGivenMondayToFriday_whenValidar_thenNoException() {
        // Given
        DadosCadastroAluguel dados = new DadosCadastroAluguel(
            LocalDate.of(2026, 2, 2),  // Segunda
            LocalDate.of(2026, 2, 6),  // Sexta
            1L,
            1L
        );

        // When & Then
        assertDoesNotThrow(() -> {
            validador.validar(dados);
        });
    }

    @Test
    @DisplayName("Aceita aluguel que termina no sábado")
    void testGivenEndingOnSaturday_whenValidar_thenNoException() {
        // Given
        DadosCadastroAluguel dados = new DadosCadastroAluguel(
            LocalDate.of(2026, 2, 2),  // Segunda
            LocalDate.of(2026, 2, 7),  // Sábado
            1L,
            1L
        );

        // When & Then
        assertDoesNotThrow(() -> {
            validador.validar(dados);
        });
    }
}
