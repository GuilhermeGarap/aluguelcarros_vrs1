package com.aluguelcarros_vrs1.domainservices.carroservices;

import com.aluguelcarros_vrs1.domain.carro.Carro;
import com.aluguelcarros_vrs1.domain.carro.DadosCadastroCarro;
import com.aluguelcarros_vrs1.domain.carro.DadosDetalhamentoCarro;
import com.aluguelcarros_vrs1.domain.carro.DadosEditarCarro;
import com.aluguelcarros_vrs1.infra.exception.ValidacaoException;
import com.aluguelcarros_vrs1.repositories.CarroRepository;
import com.aluguelcarros_vrs1.repositories.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("CarroService Tests")
class CarroServiceTest {

    @Mock
    private CarroRepository repository;

    @InjectMocks
    private CarroService services;

    private DadosCadastroCarro dadosCadastro;
    private DadosEditarCarro dadosEditar;
    private Carro carro;

    @BeforeEach
    void setup() {
        dadosCadastro = new DadosCadastroCarro(
                "Fiat",
                12.5F,
                5
        );

        dadosEditar = new DadosEditarCarro(
                "Fiat uNO",
                15.5F,
                3
        );

        carro = new Carro(dadosCadastro);
        carro.setId(1L);
    }

    @Test
    @DisplayName("Cadastro válido retorna carro")
    void testGivenDadosCadastroObject_whenSave_thenSaveCarro() {
        // Given
        given(repository.existsByModelo(dadosCadastro.modelo())).willReturn(false);

        given(repository.save(any(Carro.class))).willReturn(carro);

        // When
        var resultado = services.cadastrar(dadosCadastro);

        // Then
        assertNotNull(resultado);
        assertEquals(dadosCadastro.modelo(), resultado.modelo());

        verify(repository).save(any(Carro.class));
        verify(repository).existsByModelo(dadosCadastro.modelo());
    }

    @Test
    @DisplayName("Cadastro inválido pois já existe um outro modelo desse carro registrado")
    void testGivenDadosCadastroObjectWithAlreadyUsedModelo_whenExistsByModelo_throwValidacaoException() {
        // Given
        given(repository.existsByModelo(dadosCadastro.modelo())).willReturn(true);

        // When
        var exception = assertThrows(ValidacaoException.class, () -> {
            services.cadastrar(dadosCadastro);
        });

        // Then
        assertEquals("Esse nome de modelo de carro já está registrado!", exception.getMessage());
        verify(repository, never()).save(any(Carro.class));
    }

    @Test
    @DisplayName("Cadastro válido mas o sistema tem um erro de conexão com o banco")
    void testCarroObjectToSave_whenCadastrar_thenThrowRunTimeExceptionDataBaseConectionError() {
        // Given
        given(repository.save(any(Carro.class))).willThrow(new RuntimeException("Erro de Conexão com o banco"));

        // When
        // Then
        assertThrows(RuntimeException.class, () -> {
            services.cadastrar(dadosCadastro);
            verify(repository).save(any(Carro.class));
        });
    }

    @Test
    @DisplayName("Retornar uma lista válida de todos os carros ativos")
    void testGivenCarroList_whenFindAllByAtivoTrue_thenReturnClienteList() {
        // Given
        Carro carro1 = new Carro("Honda Civic", 14f, 5, true,5);
        carro.setAtivo(true);

        // When
        List<Carro> listaCarro = List.of(carro, carro1);
        given(repository.findAllByAtivoTrue()).willReturn(listaCarro);

        var listaTeste = services.listarAtivos();

        // Then
        assertNotNull(listaTeste);
        assertEquals(2, listaTeste.size());

        verify(repository).findAllByAtivoTrue();
    }

    @Test
    @DisplayName("Retornar uma lista vazia de todos os carros ativos")
    void testGivenEmptyCarroList_whenFindAllByAtivoTrue_thenReturnEmptyCarroList() {
        // Given

        // When
        given(repository.findAllByAtivoTrue()).willReturn(Collections.emptyList());

        var listaTeste = services.listarAtivos();

        // Then
        assertTrue(listaTeste.isEmpty());
        assertEquals(0, listaTeste.size());

        verify(repository).findAllByAtivoTrue();
    }

    @Test
    @DisplayName("Dado um ID retornar o Cliente correspondente")
    void testGivenCarroId_whenFindById_thenReturnCarroObject() {
        // Given
        given(repository.findById(carro.getId())).willReturn(Optional.of(carro));

        // When
        DadosDetalhamentoCarro carroSalvo = services.buscar(1L);

        // Then
        assertNotNull(carroSalvo);
        assertEquals("Fiat", carroSalvo.modelo());
    }

    @Test
    @DisplayName("Dado um ID retornar um erro que não existe um Carro com esse ID")
    void testGivenCarroId_whenFindById_thenValidacaoExceptionIDIsNotExistent() {
        // Given
        Long idInexistente = 5L;
        given(repository.findById(anyLong())).willReturn(Optional.empty());

        // When
        var exception = assertThrows(ValidacaoException.class, () -> {
            services.buscar(idInexistente);
        });

        // Then
        assertEquals("Não existe um carro com esse ID", exception.getMessage());
    }

    @Test
    @DisplayName("Edição válida retorna carro atualizado")
    void testGivenValidDadosEditarCarroObject_whenGetReferenceById_thenSaveCarroUpdated() {
        // Given
        given(repository.getReferenceById(anyLong())).willReturn(carro);
        given(repository.existsByModelo(dadosEditar.modelo())).willReturn(false);

        // When
        DadosDetalhamentoCarro resultado = services.atualizar(carro.getId(), dadosEditar);

        // Then
        assertNotNull(resultado);
        assertEquals(dadosEditar.modelo(), resultado.modelo());

        verify(repository).getReferenceById(carro.getId());
        verify(repository).existsByModelo(dadosEditar.modelo());
    }

    @Test
    @DisplayName("Ativação válida de carro desativado")
    void testGivenDisabledCarro_whenAtivar_thenCarroActive() {
        //Given
        carro.setAtivo(false);
        given(repository.getReferenceById(carro.getId())).willReturn(carro);

        //When
        DadosDetalhamentoCarro resultado = services.ativar(carro.getId());

        //Then
        assertEquals(resultado.ativo(), true);
        assertEquals(carro.getAtivo(), resultado.ativo());
        verify(repository).getReferenceById(carro.getId());
        verify(repository).save(carro);
    }

    @Test
    @DisplayName("Desativação válida de carro ativo")
    void testGivenActiveCarro_whenDesativar_thenCarroDisabled() {
        //Given
        given(repository.getReferenceById(carro.getId())).willReturn(carro);

        //When
        DadosDetalhamentoCarro resultado = services.desativar(carro.getId());

        //Then
        assertEquals(false, resultado.ativo());
        assertEquals(carro.getAtivo(), resultado.ativo());
        verify(repository).getReferenceById(carro.getId());
        verify(repository).save(carro);
    }
}