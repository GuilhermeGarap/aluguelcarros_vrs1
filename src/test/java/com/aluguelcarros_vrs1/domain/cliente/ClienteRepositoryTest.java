package com.aluguelcarros_vrs1.domain.cliente;

import com.aluguelcarros_vrs1.domain.endereco.Endereco;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository repository;

    @Test
    void testGivenClientObject_WhenSave_ThenReturnSavedCliente() {
        //Given
        Endereco dadosEnd1 = new Endereco("Avenida Paulista", "1000", "Centro", "Apto 101", "01310-100", "São Paulo", "SP");
        Cliente cliente = new Cliente("João Silva", "joao@email.com", "11987654321", "123.456.789-00", dadosEnd1, true);

        //When
        Cliente clienteSalvo = repository.save(cliente);

        //Then
        assertNotNull(clienteSalvo);
        assertTrue(clienteSalvo.getId() > 0);
        assertTrue((boolean) clienteSalvo.getAtivo());
    }

    @Test
    void testGivenClientList_whenFindAllByAtivoTrue_thenReturnClienteListWithOnlyActive() {
        //Given
        Endereco dadosEnd1 = new Endereco("Avenida Paulista", "1000", "Centro", "Apto 101", "01310-100", "São Paulo", "SP");
        Cliente cliente = new Cliente("João Silva", "joao@email.com", "11987654321", "123.456.789-00", dadosEnd1, true);

        Endereco dadosEnd2 = new Endereco("Rua Vergueiro", "2500", "Vila Mariana", "Apto 205", "04011-002", "São Paulo", "SP");
        Cliente cliente2 = new Cliente("Maria Santos", "maria@email.com", "11987654322", "123.456.789-01", dadosEnd2, false);;
        repository.save(cliente);
        repository.save(cliente2);

        //When

        List<Cliente> listaClientes = repository.findAllByAtivoTrue();


        //Then
        assertEquals(1, listaClientes.size());
        assertEquals(true, listaClientes.get(0).getAtivo());
    }

    @Test
    void testGivenClientId_whenFindById_thenReturnClienteOfThatId() {
        //Given
        Endereco dadosEnd1 = new Endereco("Avenida Paulista", "1000", "Centro", "Apto 101", "01310-100", "São Paulo", "SP");
        Cliente cliente = new Cliente("João Silva", "joao@email.com", "11987654321", "123.456.789-00", dadosEnd1, true);

        repository.save(cliente);

        //When
        Cliente clienteSalvoId = repository.findById(cliente.getId()).get();

        //Then
        assertNotNull(clienteSalvoId);
        assertTrue(clienteSalvoId.getId() > 0);
        assertEquals(cliente.getId(), clienteSalvoId.getId());
    }

    @Test
    void testGivenClientList_whenFindAll_thenReturnClienteList() {
        //Given
        Endereco dadosEnd1 = new Endereco("Avenida Paulista", "1000", "Centro", "Apto 101", "01310-100", "São Paulo", "SP");
        Cliente cliente = new Cliente("João Silva", "joao@email.com", "11987654321", "123.456.789-00", dadosEnd1, true);

        Endereco dadosEnd2 = new Endereco("Rua Vergueiro", "2500", "Vila Mariana", "Apto 205", "04011-002", "São Paulo", "SP");
        Cliente cliente2 = new Cliente("Maria Santos", "maria@email.com", "11987654322", "123.456.789-01", dadosEnd2, true);;
        repository.save(cliente);
        repository.save(cliente2);

        //When

        List<Cliente> listaClientes = repository.findAll();

        //Then
        assertNotNull(listaClientes);
        assertEquals(2, listaClientes.size());
    }

}