package com.aluguelcarros_vrs1.domainservices.clienteservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aluguelcarros_vrs1.domain.cliente.Cliente;
import com.aluguelcarros_vrs1.domain.cliente.ClienteRepository;
import com.aluguelcarros_vrs1.domain.cliente.DadosCadastroCliente;
import com.aluguelcarros_vrs1.domain.cliente.DadosDetalhamentoCliente;
import com.aluguelcarros_vrs1.domain.endereco.DadosEndereco;
import com.aluguelcarros_vrs1.domain.endereco.Endereco;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService services;

    private DadosCadastroCliente dadosCadastro;
    private Cliente cliente;
    private Endereco endereco;
    private DadosEndereco dadosEndereco;

    @BeforeEach
    void setup() {
        dadosEndereco = new DadosEndereco("Avenida Paulista", "1000", "Centro", "Apto 101", "01310-100", "São Paulo", "SP");
        dadosCadastro = new DadosCadastroCliente("João Silva","joao@email.com","11987654321","123.456.789-00",dadosEndereco
        );
        
        endereco = new Endereco(dadosEndereco);
        cliente = new Cliente(dadosCadastro);
    }

    @Test
    void testGivenClienteObject_whenSavePerson_thenReturnClienteObject() {
        given(repository.save(any(Cliente.class))).willReturn(cliente);

        //When
        DadosDetalhamentoCliente resultado = services.cadastrar(dadosCadastro);

        //Then
        assertNotNull(resultado, "O resultado não deve ser nulo");
        assertEquals("João Silva", resultado.nome(), "O nome deve ser igual ao cadastrado");
        assertEquals("joao@email.com", resultado.email(), "O email deve ser igual ao cadastrado");
        assertEquals("11987654321", resultado.telefone(), "O telefone deve ser igual ao cadastrado");
    }
}