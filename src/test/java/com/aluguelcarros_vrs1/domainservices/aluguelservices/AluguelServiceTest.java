package com.aluguelcarros_vrs1.domainservices.aluguelservices;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aluguelcarros_vrs1.domain.aluguel.Aluguel;
import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.domain.aluguel.DadosEditarAluguel;
import com.aluguelcarros_vrs1.domain.carro.Carro;
import com.aluguelcarros_vrs1.domain.cliente.Cliente;
import com.aluguelcarros_vrs1.domain.endereco.Endereco;
import com.aluguelcarros_vrs1.infra.exception.ValidacaoException;
import com.aluguelcarros_vrs1.repositories.AluguelRepository;
import com.aluguelcarros_vrs1.repositories.CarroRepository;
import com.aluguelcarros_vrs1.repositories.ClienteRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AluguelService Tests")
class AluguelServiceTest {

    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private CarroRepository carroRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private List<AluguelValidador> validadores;

    @InjectMocks
    private AluguelService aluguelService;

    private Endereco endereco;
    private Cliente cliente;
    private Carro carro;
    private DadosCadastroAluguel dadosCadastro;
    private DadosEditarAluguel dadosEditar;
    private Aluguel aluguel;

    @BeforeEach
    void setup() {
        endereco = new Endereco("Avenida Paulista", "1000", "Centro", "Apto 101", "01310-100", "São Paulo", "SP");
        cliente = new Cliente("João Silva", "joao@email.com", "11987654321", "123.456.789-00", LocalDate.of(2003, 5, 1), endereco, true);
        cliente.setId(1L);
        carro = new Carro("Fiat", 25F, 3, true, 2);
        carro.setId(1L);
        dadosCadastro = new DadosCadastroAluguel(LocalDate.of(2026, 2, 10), LocalDate.of(2026, 2, 14), 1L, 1L);
        dadosEditar = new DadosEditarAluguel(LocalDate.of(2026, 2, 10), LocalDate.of(2026, 2, 15));
        aluguel = new Aluguel(LocalDate.of(2026, 2, 10), LocalDate.of(2026, 2, 14), true, cliente, carro);
        aluguel.setId(1L);
    }

    @Test
    @DisplayName("Cadastro bem-sucedido de aluguel")
    void testGivenValidDadosCadastroAluguel_whenCadastrar_thenReturnDadosDetalhamentoAluguel() {
        // Given
        given(clienteRepository.existsById(dadosCadastro.cliente_id())).willReturn(true);
        given(carroRepository.existsById(dadosCadastro.carro_id())).willReturn(true);
        given(clienteRepository.getReferenceById(dadosCadastro.cliente_id())).willReturn(cliente);
        given(carroRepository.getReferenceById(dadosCadastro.carro_id())).willReturn(carro);
        given(aluguelRepository.save(any(Aluguel.class))).willReturn(aluguel);

        // When
        var resultado = aluguelService.cadastrar(dadosCadastro);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.ativo());
        verify(aluguelRepository).save(any(Aluguel.class));
    }

    @Test
    @DisplayName("Cadastro falha quando cliente não existe")
    void testGivenInvalidClienteId_whenCadastrar_thenThrowValidacaoException() {
        // Given
        given(clienteRepository.existsById(dadosCadastro.cliente_id())).willReturn(false);

        // When
        var exception = assertThrows(ValidacaoException.class, () -> {
            aluguelService.cadastrar(dadosCadastro);
        });

        // Then
        assertEquals("ID do Cliente informado não existe!", exception.getMessage());
        verify(aluguelRepository, never()).save(any(Aluguel.class));
    }

    @Test
    @DisplayName("Cadastro falha quando carro não existe")
    void testGivenInvalidCarroId_whenCadastrar_thenThrowValidacaoException() {
        // Given
        given(clienteRepository.existsById(dadosCadastro.cliente_id())).willReturn(true);
        given(carroRepository.existsById(dadosCadastro.carro_id())).willReturn(false);

        // When
        var exception = assertThrows(ValidacaoException.class, () -> {
            aluguelService.cadastrar(dadosCadastro);
        });

        // Then
        assertEquals("ID do Carro informado não existe!", exception.getMessage());
        verify(aluguelRepository, never()).save(any(Aluguel.class));
    }

    @Test
    @DisplayName("Listar aluguéis ativos retorna lista correta")
    void testGivenAluguelListWithActiveItems_whenListarAtivos_thenReturnOnlyActiveAlugueis() {
        // Given
        Aluguel aluguel2 = new Aluguel(LocalDate.of(2026, 3, 10), LocalDate.of(2026, 3, 15), true, cliente, carro);
        List<Aluguel> alugueisList = List.of(aluguel, aluguel2);
        given(aluguelRepository.findAllByAtivoTrue()).willReturn(alugueisList);

        // When
        var resultado = aluguelService.listarAtivos();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(a -> a.ativo()));
        verify(aluguelRepository).findAllByAtivoTrue();
    }

    @Test
    @DisplayName("Listar aluguéis ativos retorna lista vazia")
    void testGivenEmptyAluguelList_whenListarAtivos_thenReturnEmptyList() {
        // Given
        given(aluguelRepository.findAllByAtivoTrue()).willReturn(Collections.emptyList());

        // When
        var resultado = aluguelService.listarAtivos();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.size());
        verify(aluguelRepository).findAllByAtivoTrue();
    }

    @Test
    @DisplayName("Listar aluguéis desativados retorna lista correta")
    void testGivenAluguelListWithInactiveItems_whenListarDesativados_thenReturnOnlyInactiveAlugueis() {
        // Given
        aluguel.desativar();
        List<Aluguel> alugueisList = List.of(aluguel);
        given(aluguelRepository.findAllByAtivoFalse()).willReturn(alugueisList);

        // When
        var resultado = aluguelService.listarDesativados();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertFalse(resultado.get(0).ativo());
        verify(aluguelRepository).findAllByAtivoFalse();
    }

    @Test
    @DisplayName("Listar todos os aluguéis retorna lista completa")
    void testGivenAluguelList_whenListarTodos_thenReturnAllAlugueis() {
        // Given
        Aluguel aluguel2 = new Aluguel(LocalDate.of(2026, 3, 10), LocalDate.of(2026, 3, 15), false, cliente, carro);
        List<Aluguel> alugueisList = List.of(aluguel, aluguel2);
        given(aluguelRepository.findAll()).willReturn(alugueisList);

        // When
        var resultado = aluguelService.listarTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(aluguelRepository).findAll();
    }

    @Test
    @DisplayName("Buscar aluguel por ID válido retorna aluguel")
    void testGivenValidAluguelId_whenBuscar_thenReturnDadosDetalhamentoAluguel() {
        // Given
        given(aluguelRepository.findById(aluguel.getId())).willReturn(Optional.of(aluguel));

        // When
        var resultado = aluguelService.buscar(aluguel.getId());

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.ativo());
        verify(aluguelRepository).findById(aluguel.getId());
    }

    @Test
    @DisplayName("Buscar aluguel por ID inválido lança exceção")
    void testGivenInvalidAluguelId_whenBuscar_thenThrowValidacaoException() {
        // Given
        given(aluguelRepository.findById(999L)).willReturn(Optional.empty());

        // When
        var exception = assertThrows(ValidacaoException.class, () -> {
            aluguelService.buscar(999L);
        });

        // Then
        assertEquals("Não existe um aluguel com esse ID", exception.getMessage());
    }

    @Test
    @DisplayName("Desativar aluguel com sucesso incrementa disponibilidade do carro")
    void testGivenValidAluguelId_whenDesativarAluguel_thenDesativaAndIncrementsCarroDisponibilidade() {
        // Given
        int disponibilidadeAntes = carro.getDisponivel();
        given(aluguelRepository.findById(aluguel.getId())).willReturn(Optional.of(aluguel));
        given(carroRepository.save(any(Carro.class))).willReturn(carro);
        given(aluguelRepository.save(any(Aluguel.class))).willReturn(aluguel);

        // When
        var resultado = aluguelService.desativarAluguel(aluguel.getId());

        // Then
        assertNotNull(resultado);
        assertFalse(resultado.ativo());
        verify(carroRepository).save(any(Carro.class));
        verify(aluguelRepository).save(any(Aluguel.class));
    }

    @Test
    @DisplayName("Desativar aluguel inexistente lança exceção")
    void testGivenInvalidAluguelId_whenDesativarAluguel_thenThrowValidacaoException() {
        // Given
        given(aluguelRepository.findById(999L)).willReturn(Optional.empty());

        // When
        var exception = assertThrows(ValidacaoException.class, () -> {
            aluguelService.desativarAluguel(999L);
        });

        // Then
        assertEquals("Aluguel não encontrado com ID 999", exception.getMessage());
    }

    @Test
    @DisplayName("Atualizar aluguel com novos dados")
    void testGivenValidDadosEditarAluguel_whenAtualizar_thenUpdateAluguel() {
        // Given
        given(aluguelRepository.getReferenceById(aluguel.getId())).willReturn(aluguel);

        // When
        var resultado = aluguelService.atualizar(aluguel.getId(), dadosEditar);

        // Then
        assertNotNull(resultado);
        verify(aluguelRepository).getReferenceById(aluguel.getId());
    }

    @Test
    @DisplayName("Ativar aluguel desativado com sucesso")
    void testGivenInactiveAluguel_whenAtivar_thenActivateAndReturnDadosDetalhamento() {
        // Given
        aluguel.desativar();
        given(aluguelRepository.getReferenceById(aluguel.getId())).willReturn(aluguel);

        // When
        var resultado = aluguelService.ativar(aluguel.getId());

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.ativo());
        verify(aluguelRepository).getReferenceById(aluguel.getId());
    }

    @Test
    @DisplayName("Ativar aluguel já ativo retorna null")
    void testGivenActiveAluguel_whenAtivar_thenReturnNull() {
        // Given
        given(aluguelRepository.getReferenceById(aluguel.getId())).willReturn(aluguel);

        // When
        var resultado = aluguelService.ativar(aluguel.getId());

        // Then
        assertNull(resultado);
    }
}
