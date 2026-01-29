package com.aluguelcarros_vrs1.domainservices.aluguelservices;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.domainservices.ValidacaoException;

@ExtendWith(MockitoExtension.class)
class AluguelValidadorDataTest {

    @InjectMocks
    private AluguelValidadorData validador;

    @Test
    @DisplayName("Lança exceção quando a data de início for um domingo")
    void testGivenStartDateIsSunday_whenValidar_thenThrowValidacaoException() {
        var dataInicioDomingo = LocalDate.of(2025, 1, 19);
        var dataTerminoSegunda = LocalDate.of(2025, 1, 20);

        var dados = new DadosCadastroAluguel(dataInicioDomingo, dataTerminoSegunda, 1L, 1L);

        assertThrows(ValidacaoException.class, () -> validador.validar(dados));
    }

    @Test
    @DisplayName("Lança exceção quando a data de término for um domingo")
    void testGivenEndDateIsSunday_whenValidar_thenThrowValidacaoException() {

        var dataInicioSabado = LocalDate.of(2025, 1, 18);
        var dataTerminoDomingo = LocalDate.of(2025, 1, 19);

        var dados = new DadosCadastroAluguel(dataInicioSabado, dataTerminoDomingo, 1L, 1L);

        assertThrows(ValidacaoException.class, () -> validador.validar(dados));
    }

    @Test
    @DisplayName("Não lança exceção quando nenhuma data for domingo")
    void testGivenValidDates_whenValidar_thenDoesNotThrowException() {

        var dataInicioSegunda = LocalDate.of(2025, 1, 20);
        var dataTerminoTerca = LocalDate.of(2025, 1, 21);

        var dados = new DadosCadastroAluguel(dataInicioSegunda, dataTerminoTerca, 1L, 1L);

        assertDoesNotThrow(() -> validador.validar(dados));
    }
}