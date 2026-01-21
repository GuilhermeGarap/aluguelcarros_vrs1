package com.aluguelcarros_vrs1.domainservices.aluguelservices;

import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.domain.carro.Carro;
import com.aluguelcarros_vrs1.domain.carro.CarroRepository;
import com.aluguelcarros_vrs1.domainservices.ValidacaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AluguelLogicaCarroDisponivelTest {

    @InjectMocks
    private AluguelLogicaCarroDisponivel validador;

    @Mock
    private CarroRepository carroRepository;

    @Mock
    private Carro carro;

    @Mock
    private DadosCadastroAluguel dadosCadastroAluguel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startDadosAtributos();
    }

    @Test
    @DisplayName("Não lança nenhuma excessão e o carro é validado com sucesso")
    void testeValidarCarroSucesso() {

        when(carroRepository.getReferenceById(dadosCadastroAluguel.carro_id())).thenReturn(carro);
        when(carro.getDisponivel()).thenReturn(5);

        validador.validar(dadosCadastroAluguel);

        verify(carro).setDisponivel(4);
        verify(carroRepository).save(carro);

    }

    @Test
    @DisplayName("Lança uma excessão quando o carro não tem unidades disponiveis")
    void testeValidarSemCarroDisponivel() {

        when(carroRepository.getReferenceById(dadosCadastroAluguel.carro_id())).thenReturn(carro);
        when(carro.getDisponivel()).thenReturn(0);

        assertThrows(ValidacaoException.class, () -> validador.validar(dadosCadastroAluguel));

        verify(carroRepository, never()).save(any());

    }

    public void startDadosAtributos() {
        dadosCadastroAluguel = new DadosCadastroAluguel(
                LocalDate.now(),
                LocalDate.now().
                        plusDays(1), 1L, 1L
        );
    }
}