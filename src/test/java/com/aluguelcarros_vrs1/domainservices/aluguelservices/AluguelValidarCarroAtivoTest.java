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
import com.aluguelcarros_vrs1.domain.carro.Carro;
import com.aluguelcarros_vrs1.infra.exception.ValidacaoException;
import com.aluguelcarros_vrs1.repositories.CarroRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AluguelValidarCarroAtivo Tests")
class AluguelValidarCarroAtivoTest {

    @Mock
    private CarroRepository carroRepository;

    @InjectMocks
    private AluguelValidarCarroAtivo validador;

    private DadosCadastroAluguel dados;
    private Carro carroAtivo;
    private Carro carroInativo;

    @BeforeEach
    void setup() {
        dados = new DadosCadastroAluguel(
            LocalDate.of(2026, 2, 2),
            LocalDate.of(2026, 2, 6),
            1L,
            1L
        );

        carroAtivo = new Carro("Fiat Uno", 15F, 5, true, 3);
        carroAtivo.setId(1L);

        carroInativo = new Carro("Honda Civic", 20F, 5, false, 2);
        carroInativo.setId(2L);
    }

    @Test
    @DisplayName("Rejeita aluguel com carro inativo")
    void testGivenInactiveCarro_whenValidar_thenThrowValidacaoException() {
        // Given
        dados = new DadosCadastroAluguel(
            LocalDate.of(2026, 2, 2),
            LocalDate.of(2026, 2, 6),
            1L,
            2L
        );
        given(carroRepository.findAtivoById(2L)).willReturn(false);

        // When & Then
        var exception = assertThrows(ValidacaoException.class, () -> {
            validador.validar(dados);
        });
        assertEquals("Esse carro está desativado no sistema", exception.getMessage());
    }

    @Test
    @DisplayName("Aceita aluguel com carro ativo")
    void testGivenActiveCarro_whenValidar_thenNoException() {
        // Given
        given(carroRepository.findAtivoById(1L)).willReturn(true);

        // When & Then
        assertDoesNotThrow(() -> {
            validador.validar(dados);
        });
    }
}
