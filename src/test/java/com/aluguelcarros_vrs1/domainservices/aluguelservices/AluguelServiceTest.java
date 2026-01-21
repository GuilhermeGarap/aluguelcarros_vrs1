package com.aluguelcarros_vrs1.domainservices.aluguelservices;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.aluguelcarros_vrs1.domain.aluguel.Aluguel;
import com.aluguelcarros_vrs1.domain.aluguel.AluguelRepository;
import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.domain.aluguel.DadosDetalhamentoAluguel;
import com.aluguelcarros_vrs1.domain.carro.Carro;
import com.aluguelcarros_vrs1.domain.carro.CarroRepository;
import com.aluguelcarros_vrs1.domain.cliente.Cliente;
import com.aluguelcarros_vrs1.domain.cliente.ClienteRepository;
import com.aluguelcarros_vrs1.domainservices.ValidacaoException;

@SpringBootTest
public class AluguelServiceTest {

    @InjectMocks
    private AluguelService aluguelService;

    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private CarroRepository carroRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private AluguelLogicaCarroDisponivel logica;

    @Mock
    private List<AluguelValidador> validadores;

    @Mock
    private AluguelLogicaCarroDisponivel aluguelLogicaCarroDisponivel;


    private DadosDetalhamentoAluguel dadosDetalhamentoAluguel;
    private DadosCadastroAluguel dadosCadastroAluguel;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startDadosAtributos();

    }


    @Test
    @DisplayName("O cadastro de aluguel é sucedido")
    void testeCadastrarSucedido() {

        Cliente clienteMock = Mockito.mock(Cliente.class);
        Carro carroMock = Mockito.mock(Carro.class);

        when(clienteRepository.existsById(1L)).thenReturn(true);
        when(carroRepository.existsById(1L)).thenReturn(true);
        when(clienteRepository.getReferenceById(1L)).thenReturn(clienteMock);
        when(carroRepository.getReferenceById(1L)).thenReturn(carroMock);

        validadores.forEach(v -> Mockito.doNothing().when(v).validar(any(DadosCadastroAluguel.class)));
        
        DadosDetalhamentoAluguel resultado = aluguelService.cadastrar(dadosCadastroAluguel);

        assertNotNull(resultado, "O resultado do cadastro não pode ser nulo");

        verify(aluguelRepository, Mockito.times(1)).save(any(Aluguel.class));
        }

    @Test
    @DisplayName("Lança uma excessão que o id de Cliente não existe")
    void testeCadastrarClienteNãoExiste() {
        when(clienteRepository.existsById(1L)).thenReturn(false);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () ->
                aluguelService.cadastrar(dadosCadastroAluguel));

        assertEquals("ID do Cliente informado não existe!", exception.getMessage());

    }

    @Test
    @DisplayName("Lança uma excessão que o id de Carro não existe")
    void testeCadastrarCarroNãoExiste() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        when(carroRepository.existsById(1L)).thenReturn(false);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () ->
                aluguelService.cadastrar(dadosCadastroAluguel));

        assertEquals("ID do Carro informado não existe!", exception.getMessage());

    }

    @Test
    @DisplayName("Desativa os alugueis expirados automaticamente")
    void testDesativarAlugueisExpirados() {
        Aluguel aluguelExpirado = Mockito.mock(Aluguel.class);
        Aluguel aluguelNoPrazo = Mockito.mock(Aluguel.class);

        when(aluguelExpirado.getData_termino()).thenReturn(LocalDate.now().minusDays(1));
        when(aluguelNoPrazo.getData_termino()).thenReturn(LocalDate.now().plusDays(1));

        when(aluguelRepository.findAllByAtivoTrue()).thenReturn(List.of(aluguelExpirado, aluguelNoPrazo));

        aluguelService.desativarAlugueisExpirados();

        verify(aluguelExpirado).desativar();
        verify(aluguelRepository).save(aluguelExpirado);

        verify(aluguelNoPrazo, Mockito.never()).desativar();
    }

    @Test
    @DisplayName("Desativa o aluguel de ID enviado")
    void testDesativarAluguelSucedido() {
        Long idAluguel = 1L;

        Aluguel aluguelMock = Mockito.mock(Aluguel.class);
        Carro carroMock = Mockito.mock(Carro.class);

        when(aluguelRepository.findById(idAluguel)).thenReturn(java.util.Optional.of(aluguelMock));

        when(aluguelMock.getCarro()).thenReturn(carroMock);

        when(carroMock.getDisponivel()).thenReturn(4);
        when(carroMock.getUnidades()).thenReturn(5); 

        aluguelService.desativarAluguel(idAluguel);

        verify(aluguelMock).desativar();

        verify(carroMock).setDisponivel(5);

        verify(carroRepository).save(carroMock);
        verify(aluguelRepository).save(aluguelMock);
    }

    @Test
    @DisplayName("Falha em desativar o aluguel, pois esse ID de aluguel não existe")
    void testDesativarAluguelNãoEncontraIdDeAluguel() {
        Long idInexistente = 2L;
        Aluguel aluguelMock = Mockito.mock(Aluguel.class);

        when(aluguelRepository.findById(2L)).thenReturn(java.util.Optional.empty());

        ValidacaoException exception = assertThrows(ValidacaoException.class, () ->
                aluguelService.desativarAluguel(idInexistente));

        assertEquals("Aluguel não encontrado com ID " + idInexistente, exception.getMessage());

        verify(aluguelRepository, Mockito.never()).save(any());
        
    }

    @Test
    @DisplayName("Falha em desativar o aluguel, pois o estoque ficaria negativo ou com valor zero")
    void testDesativarAluguelEstoqueInconsiste() {
        Long idAluguel = 1L;
        Aluguel aluguelMock = Mockito.mock(Aluguel.class);
        Carro carroMock = Mockito.mock(Carro.class);

        when(aluguelRepository.findById(1L)).thenReturn(java.util.Optional.of(aluguelMock));
        when(aluguelMock.getCarro()).thenReturn(carroMock);

        when(carroMock.getDisponivel()).thenReturn(4);
        when(carroMock.getUnidades()).thenReturn(4);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () ->
                aluguelService.desativarAluguel(idAluguel));

        assertEquals("Não é possível devolver um carro mais vezes que a quantidade disponivel em estoque.", exception.getMessage());


    }

    private void startDadosAtributos() {
        dadosCadastroAluguel = new DadosCadastroAluguel(
            LocalDate.parse("2026-01-21"), 
            LocalDate.parse("2026-01-28"), 
            1L, 
            1L
        );
    }
}
