package com.aluguelcarros_vrs1.domainservices.clienteservices;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aluguelcarros_vrs1.domain.cliente.Cliente;
import com.aluguelcarros_vrs1.domain.cliente.ClienteRepository;
import com.aluguelcarros_vrs1.domain.cliente.DadosCadastroCliente;
import com.aluguelcarros_vrs1.domain.cliente.DadosDetalhamentoCliente;
import com.aluguelcarros_vrs1.domain.cliente.DadosEditarCliente;
import com.aluguelcarros_vrs1.domain.endereco.DadosEndereco;
import com.aluguelcarros_vrs1.domain.endereco.Endereco;
import com.aluguelcarros_vrs1.domainservices.ValidacaoException;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteService Tests")
@SuppressWarnings("all")
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService services;

    private DadosCadastroCliente dadosCadastro;
    private DadosEditarCliente dadosEditar;
    private Cliente cliente;
    private Endereco endereco;
    private DadosEndereco dadosEndereco;

    @BeforeEach
    void setup() {
        dadosEndereco = new DadosEndereco("Avenida Paulista", "1000", "Centro", "Apto 101", "01310-100", "São Paulo", "SP");
        dadosCadastro = new DadosCadastroCliente(
            "Roberto dos Testes",
            "robertT@email.com",
            "1155443322",
            "123.456.789-12",
            dadosEndereco
        );
        
        dadosEditar = new DadosEditarCliente(
            "JRoberto dos Testes Atualizado",
            "11987654999",
            dadosEndereco
        );

        endereco = new Endereco(dadosEndereco);
        cliente = new Cliente(dadosCadastro);
        cliente.setId(1L);
    }

    @Test
    @DisplayName("Cadastro válido retorna cliente")
    void testGivenDadosCadastroObject_whenSave_thenSaveCliente() {
        // Given
        given(repository.existsByCpf(dadosCadastro.cpf())).willReturn(false);
        given(repository.existsByNome(dadosCadastro.nome())).willReturn(false);
        given(repository.existsByTelefone(dadosCadastro.telefone())).willReturn(false);
        given(repository.existsByEmail(dadosCadastro.email())).willReturn(false);

        given(repository.save(any(Cliente.class))).willReturn(cliente);

        // When
        var resultado = services.cadastrar(dadosCadastro);

        // Then
        assertNotNull(resultado);
        assertEquals(dadosCadastro.nome(), resultado.nome());

        verify(repository).save(any(Cliente.class));
        verify(repository).existsByCpf(dadosCadastro.cpf());
    }

    @Test
    @DisplayName("Cadastro inválido pois já existe um outro cliente registrado com esse email")
    void testGivenDadosCadastroObjectWithAlreadyUsedEmail_whenExistsByEmail_throwValidacaoException() {
        // Given
        given(repository.existsByCpf(dadosCadastro.cpf())).willReturn(false);
        given(repository.existsByEmail(dadosCadastro.email())).willReturn(true);

        // When
        var exception = assertThrows(ValidacaoException.class, () -> {
            services.cadastrar(dadosCadastro);
        });

        // Then
        assertEquals("Já existe um cliente cadastrado com esse email!", exception.getMessage());
        verify(repository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Cadastro inválido pois já existe um outro cliente registrado com esse cpf")
    void testGivenDadosCadastroObjectWithAlreadyUsedCpf_whenExistsByCpf_throwValidacaoException() {
        // Given
        given(repository.existsByCpf(dadosCadastro.cpf())).willReturn(true);

        // When
        var exception = assertThrows(ValidacaoException.class, () -> {
            services.cadastrar(dadosCadastro);
        });

        // Then
        assertEquals("Já existe um cliente cadastrado com esse CPF!", exception.getMessage());
        verify(repository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Cadastro inválido pois já existe um outro cliente registrado com esse nome")
    void testGivenDadosCadastroObjectWithAlreadyUsedNome_whenExistsByNome_throwValidacaoException() {
        // Given
        given(repository.existsByCpf(dadosCadastro.cpf())).willReturn(false);
        given(repository.existsByEmail(dadosCadastro.email())).willReturn(false);
        given(repository.existsByNome(dadosCadastro.nome())).willReturn(true);

        // When
        var exception = assertThrows(ValidacaoException.class, () -> {
            services.cadastrar(dadosCadastro);
        });

        // Then
        assertEquals("Já existe um cliente cadastrado com esse nome!", exception.getMessage());
        verify(repository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Cadastro inválido pois já existe um outro cliente registrado com esse telefone")
    void testGivenDadosCadastroObjectWithAlreadyUsedTelefone_whenExistsByTelefone_throwValidacaoException() {
        // Given
        given(repository.existsByCpf(dadosCadastro.cpf())).willReturn(false);
        given(repository.existsByEmail(dadosCadastro.email())).willReturn(false);
        given(repository.existsByNome(dadosCadastro.nome())).willReturn(false);
        given(repository.existsByTelefone(dadosCadastro.telefone())).willReturn(true);

        // When
        var exception = assertThrows(ValidacaoException.class, () -> {
            services.cadastrar(dadosCadastro);
        });

        // Then
        assertEquals("Já existe um cliente cadastrado com esse telefone!", exception.getMessage());
        verify(repository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Cadastro válido mas o sistema tem um erro de conexão com o banco")
    void testClienteObjectToSave_whenCadastrar_thenThrowRunTimeExceptionDataBaseConectionError() {
        // Given
        given(repository.save(any(Cliente.class))).willThrow(new RuntimeException("Erro de Conexão com o banco"));

        // When
        // Then
        assertThrows(RuntimeException.class, () -> {
            services.cadastrar(dadosCadastro);
        });
    }

    @Test
    @DisplayName("Retornar uma lista válida de todos os clientes ativos")
    void testGivenClientenList_whenFindAllByAtivoTrue_thenReturnClienteList() {
        // Given
        Cliente cliente2 = new Cliente("Marcos Testes", "12999999999", "marcosteste@gmail.com", "108.172.710-15", endereco, true);

        cliente.setAtivo(true);
        cliente2.setAtivo(true);

        // When
        List<Cliente> listaClientes = List.of(cliente, cliente2);
        given(repository.findAllByAtivoTrue()).willReturn(listaClientes);

        var listaTeste = services.listarAtivos();

        // Then
        assertNotNull(listaTeste);
        assertEquals(2, listaTeste.size());

        verify(repository).findAllByAtivoTrue();
    }

    @Test
    @DisplayName("Retornar uma lista vazia de todos os clientes ativos")
    void testGivenEmptyClientenList_whenFindAllByAtivoTrue_thenReturnEmptyClienteList() {
        // Given
        Cliente cliente2 = new Cliente("Marcos Testes", "12999999999", "marcosteste@gmail.com", "108.172.710-15", endereco, true);

        cliente.setAtivo(true);
        cliente2.setAtivo(true);

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
    void testGivenClienteId_whenFindById_thenReturnClienteObject() {
        // Given
        given(repository.findById(cliente.getId())).willReturn(Optional.of(cliente));

        // When
        DadosDetalhamentoCliente clienteSalvo = services.buscar(1L);

        // Then
        assertNotNull(clienteSalvo);
        assertEquals("Roberto dos Testes", clienteSalvo.nome());
    }

    @Test
    @DisplayName("Dado um ID retornar um erro que não existe um Cliente com esse ID")
    void testGivenClienteId_whenFindById_thenValidacaoExceptionIDIsNotExistent() {
        // Given
        Long idInexistente = 5L;
        given(repository.findById(anyLong())).willReturn(Optional.empty());

        // When
        var exception = assertThrows(ValidacaoException.class, () -> {
            services.buscar(idInexistente);
        });

        // Then
        assertEquals("Cliente não encontrado com o ID: " + idInexistente, exception.getMessage());
    }

    // @Test
    // @DisplayName("Edição válida retorna cliente atualizado")
    // void testGivenValidDadosEditarClienteObject_whenGetReferenceById_thenSaveClienteUpdated() {
    //     // Given
    //     given(repository.existsByNome(dadosCadastro.nome())).willReturn(false);
    //     given(repository.existsByTelefone(dadosCadastro.telefone())).willReturn(false);

    //     given(cliente.atualizarInformacoes())
    //     given(repository.save(any(Cliente.class))).willReturn(cliente);

    //     // When
    //     var resultado = services.cadastrar(dadosCadastro);

    //     // Then
    //     assertNotNull(resultado);
    //     assertEquals(dadosCadastro.nome(), resultado.nome());

    //     verify(repository).save(any(Cliente.class));
    //     verify(repository).existsByCpf(dadosCadastro.cpf());
    // }
}