package com.aluguelcarros_vrs1;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.aluguelcarros_vrs1.domain.cliente.Cliente;
import com.aluguelcarros_vrs1.domain.cliente.DadosCadastroCliente;
import com.aluguelcarros_vrs1.domain.endereco.DadosEndereco;

@SpringBootTest
@DisplayName("Testes da classe Cliente")
class ClienteTest {
    
    private Cliente cliente;
    private DadosEndereco endereco;
    
    @BeforeEach
    void setup() {
        // Arrange: Preparar dados que serão usados
        this.endereco = new DadosEndereco(
            "Avenida Paulista",
            "1000",
            "Centro",
            "Apto 101",
            "01310-100",
            "São Paulo",
            "SP"
        );
    }
    
    @Test
    @DisplayName("Deve criar um cliente com status ativo")
    void testClienteCriadoComStatusAtivo() {
        // Arrange já feito no setup
        
        // Act: Criar o cliente
        DadosCadastroCliente dados = new DadosCadastroCliente(
            "João Silva",
            "joao@email.com",
            "11987654321",
            "123.456.789-00",
            endereco
        );
        cliente = new Cliente(dados);
        
        // Assert: Verificar se está realmente ativo
        assertTrue(cliente.getAtivo(), "Cliente deveria estar ativo ao ser criado");
    }
    
    @Test
    @DisplayName("Deve desativar um cliente corretamente")
    void testDesativarCliente() {
        // Arrange
        DadosCadastroCliente dados = new DadosCadastroCliente(
            "Maria Silva",
            "maria@email.com",
            "11987654322",
            "123.456.789-01",
            endereco
        );
        cliente = new Cliente(dados);
        
        // Act: Desativar
        cliente.desativar();
        
        // Assert: Verificar se foi desativado
        assertFalse(cliente.getAtivo(), "Cliente deveria estar inativo após desativar");
    }
}