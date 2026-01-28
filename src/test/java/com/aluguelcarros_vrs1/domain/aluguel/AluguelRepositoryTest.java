package com.aluguelcarros_vrs1.domain.aluguel;

import com.aluguelcarros_vrs1.domain.carro.Carro;
import com.aluguelcarros_vrs1.domain.carro.CarroRepository;
import com.aluguelcarros_vrs1.domain.cliente.Cliente;
import com.aluguelcarros_vrs1.domain.cliente.ClienteRepository;
import com.aluguelcarros_vrs1.domain.endereco.DadosEndereco;
import com.aluguelcarros_vrs1.domain.endereco.Endereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AluguelRepositoryTest {

    @Autowired
    AluguelRepository repository;

    @Autowired
    CarroRepository carroRepository;

    @Autowired
    ClienteRepository clienteRepository;

    Endereco dadosEnd1;
    Cliente cliente;
    Carro carro;
    Aluguel aluguel;

    @BeforeEach
    void setup() {
        dadosEnd1 = new Endereco("Avenida Paulista", "1000", "Centro", "Apto 101", "01310-100", "São Paulo", "SP");
        cliente = new Cliente("João Silva", "joao@email.com", "11987654321", "123.456.789-00", dadosEnd1, true);
        carro = new Carro("Fiat", 25F, 3, true, 2);
        aluguel = new Aluguel(LocalDate.of(2026, 1, 20), LocalDate.of(2026, 1, 25), true, cliente, carro);

    }

    @Test
    void givenAluguelObject_whenSave_thenReturnSavedAluguel() {
        //Given

        //Then
        Aluguel aluguelSalvo = repository.save(aluguel);

        //When
        assertNotNull(aluguelSalvo);
        assertEquals(aluguel.getId(), aluguelSalvo.getId());
        assertTrue(aluguel.getAtivo());
        assertEquals(aluguel.getCarro().getId(), aluguelSalvo.getCarro().getId());
        assertEquals(aluguel.getCliente().getId(), aluguelSalvo.getCliente().getId());
    }

    @Test
    void givenAluguelList_whenFindAllByAtivo_thenReturnAluguelListWithOnlyAlugueisAtivos() {
        //Given
        Endereco dadosEnd2 = new Endereco("Avenida Paulista", "1000", "Centro", "Apto 101", "01310-100", "São Paulo", "SP");
        Cliente cliente2 = new Cliente("João Silva", "joao@email.com", "11987654321", "123.456.789-00", dadosEnd1, true);
        Carro carro2 = new Carro("Fiat", 25F, 3, true, 2);
        Aluguel aluguel2 = new Aluguel(LocalDate.of(2026, 1, 20), LocalDate.of(2026, 1, 25), false, cliente, carro);

        clienteRepository.save(cliente);
        clienteRepository.save(cliente2);
        carroRepository.save(carro);
        carroRepository.save(carro2);

        repository.save(aluguel);
        repository.save(aluguel2);

        //Then
        List<Aluguel> listaAlugueis = repository.findAllByAtivoTrue();

        //When
        assertEquals(1, listaAlugueis.size());
        assertEquals(true, listaAlugueis.get(0).getAtivo());
    }

    @Test
    void givenAluguelList_whenFindAllByFalse_thenReturnAluguelListWithOnlyAlugueisDesativados() {
        //Given
        Endereco dadosEnd2 = new Endereco("Avenida Paulista", "1000", "Centro", "Apto 101", "01310-100", "São Paulo", "SP");
        Cliente cliente2 = new Cliente("João Silva", "joao@email.com", "11987654321", "123.456.789-00", dadosEnd1, true);
        Carro carro2 = new Carro("Fiat", 25F, 3, true, 2);
        Aluguel aluguel2 = new Aluguel(LocalDate.of(2026, 1, 20), LocalDate.of(2026, 1, 25), false, cliente, carro);

        clienteRepository.save(cliente);
        clienteRepository.save(cliente2);
        carroRepository.save(carro);
        carroRepository.save(carro2);

        repository.save(aluguel);
        repository.save(aluguel2);

        //Then
        List<Aluguel> listaAlugueis = repository.findAllByAtivoFalse();

        //When
        assertEquals(1, listaAlugueis.size());
        assertEquals(false, listaAlugueis.get(0).getAtivo());
    }

    @Test
    void givenAluguelList_whenFindAll_thenReturnAluguelList() {
        //Given

        Endereco dadosEnd2 = new Endereco("Avenida Paulista", "1000", "Centro", "Apto 101", "01310-100", "São Paulo", "SP");
        Cliente cliente2 = new Cliente("João Silva", "joao@email.com", "11987654321", "123.456.789-00", dadosEnd1, true);
        Carro carro2 = new Carro("Fiat", 25F, 3, true, 2);
        Aluguel aluguel2 = new Aluguel(LocalDate.of(2026, 1, 20), LocalDate.of(2026, 1, 25), false, cliente, carro);

        clienteRepository.save(cliente);
        clienteRepository.save(cliente2);
        carroRepository.save(carro);
        carroRepository.save(carro2);

        repository.save(aluguel);
        repository.save(aluguel2);

        //Then
        List<Aluguel> listaAlugueis = repository.findAll();

        //When
        assertEquals(2, listaAlugueis.size());
    }
}