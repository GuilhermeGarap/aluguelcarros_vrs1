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
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.domain.carro.Carro;
import com.aluguelcarros_vrs1.infra.exception.ValidacaoException;
import com.aluguelcarros_vrs1.repositories.CarroRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AluguelLogicaCarroDisponivel Tests")
class AluguelLogicaCarroDisponivelTest {

    @Mock
    private CarroRepository carroRepository;

    @InjectMocks
    private AluguelLogicaCarroDisponivel validador;

    private DadosCadastroAluguel dados;
    private Carro carroComDisponibilidade;
    private Carro carroSemDisponibilidade;

    @BeforeEach
    void setup() {
        dados = new DadosCadastroAluguel(
            LocalDate.of(2026, 2, 2),
            LocalDate.of(2026, 2, 6),
            1L,
            1L
        );

        carroComDisponibilidade = new Carro("Fiat Uno", 15F, 5, true, 3);
        carroComDisponibilidade.setId(1L);

        carroSemDisponibilidade = new Carro("Honda Civic", 20F, 5, true, 0);
        carroSemDisponibilidade.setId(2L);
    }

    @Test
    @DisplayName("Rejeita aluguel se carro não tem disponibilidade")
    void testGivenCarroWithoutAvailability_whenValidar_thenThrowValidacaoException() {
        // Given
        dados = new DadosCadastroAluguel(
            LocalDate.of(2026, 2, 2),
            LocalDate.of(2026, 2, 6),
            1L,
            2L
        );
        given(carroRepository.getReferenceById(2L)).willReturn(carroSemDisponibilidade);

        // When & Then
        var exception = assertThrows(ValidacaoException.class, () -> {
            validador.validar(dados);
        });
        assertEquals("Não há carros disponíveis para este modelo.", exception.getMessage());
    }

    @Test
    @DisplayName("Aceita aluguel se carro tem disponibilidade")
    void testGivenCarroWithAvailability_whenValidar_thenNoException() {
        // Given
        given(carroRepository.getReferenceById(1L)).willReturn(carroComDisponibilidade);

        // When & Then
        assertDoesNotThrow(() -> {
            validador.validar(dados);
        });
    }

    @Test
    @DisplayName("Decrementa corretamente a disponibilidade do carro")
    void testGivenCarroWithAvailability_whenValidar_thenDecrementsDisponibilidade() {
        // Given
        int disponibilidadeAntes = carroComDisponibilidade.getDisponivel();
        given(carroRepository.getReferenceById(1L)).willReturn(carroComDisponibilidade);

        // When
        validador.validar(dados);

        // Then
        assertEquals(disponibilidadeAntes - 1, carroComDisponibilidade.getDisponivel());
        verify(carroRepository).save(carroComDisponibilidade);
    }

    @Test
    @DisplayName("Decrementa disponibilidade a zero e rejeita próximo aluguel")
    void testGivenCarroWithOnlyOneAvailability_whenValidarTwice_thenSecondThrowsException() {
        // Given
        Carro carroComUmaDisponibilidade = new Carro("Fiat Uno", 15F, 5, true, 1);
        carroComUmaDisponibilidade.setId(1L);
        dados = new DadosCadastroAluguel(
            LocalDate.of(2026, 2, 2),
            LocalDate.of(2026, 2, 6),
            1L,
            1L
        );

        given(carroRepository.getReferenceById(1L)).willReturn(carroComUmaDisponibilidade);

        // When - Primeiro aluguel
        assertDoesNotThrow(() -> {
            validador.validar(dados);
        });

        // Then - Disponibilidade virou 0
        assertEquals(0, carroComUmaDisponibilidade.getDisponivel());

        // When - Segundo aluguel
        var exception = assertThrows(ValidacaoException.class, () -> {
            validador.validar(dados);
        });

        // Then
        assertEquals("Não há carros disponíveis para este modelo.", exception.getMessage());
    }
}
