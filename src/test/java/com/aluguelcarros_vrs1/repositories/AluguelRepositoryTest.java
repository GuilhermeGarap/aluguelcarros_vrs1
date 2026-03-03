package com.aluguelcarros_vrs1.repositories;

import com.aluguelcarros_vrs1.domain.Aluguel;
import com.aluguelcarros_vrs1.domain.Carro;
import com.aluguelcarros_vrs1.domain.Cliente;
import com.aluguelcarros_vrs1.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
class AluguelRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    AluguelRepository repository;

    @Autowired
    CarroRepository carroRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    UserRepository userRepository;

    Cliente cliente;
    Carro carro;
    List<Aluguel.AluguelStatus> statusList;
    User user;
    Aluguel aluguel;


    @BeforeEach
    void instanciaUnica() {
        cliente =  clienteRepository.save(new Cliente());
        carro = carroRepository.save(new Carro());
        statusList = new ArrayList<Aluguel.AluguelStatus>();
        statusList.add(Aluguel.AluguelStatus.ATIVO);
        user = new User();
        user.setUsername("Teste");
        userRepository.save(user);

        aluguel = new Aluguel(null, LocalDate.of(2026, 2, 15), LocalDate.of(2026, 3, 20), cliente, carro, statusList, Instant.now(), Instant.now(), user.getUsername());
    }

    Aluguel instanciaLista() {
        Cliente cliente1 =  clienteRepository.save(new Cliente());
        Carro carro1 = carroRepository.save(new Carro());
        return new Aluguel(null, LocalDate.of(2026, 2, 15), LocalDate.of(2026, 3, 20), cliente1, carro1, statusList, Instant.now(), Instant.now(), user.getUsername());
    }

    @DisplayName("Should Save Aluguel")
    @Test
    void testGivenAluguelObject_whenAluguelStudent_thenReturnSavedAluguel() {
        //Given
        //BeforeEach

        //When
        Aluguel savedAluguel = repository.save(aluguel);

        //Then
        assertNotNull(savedAluguel);
        assertTrue(savedAluguel.getId() > 0);
        assertTrue(savedAluguel.getCreatedAt().isBefore(Instant.now()));
        assertThat(savedAluguel.getAluguelStatus()).contains(Aluguel.AluguelStatus.ATIVO);
    }

    @DisplayName("Should Return All Alugueis When No Filters Apllied")
    @Test
    void testGivenAluguelList_whenFindAll_thenReturnAluguelList() {
        //Given
        //BeforeEach
        Aluguel aluguel1 = instanciaLista();
        repository.saveAll(List.of(aluguel, aluguel1));

        //When
        List<Aluguel> aluguelList = repository.findAll();

        //Then
        assertNotNull(aluguelList);
        assertEquals(2, aluguelList.size());
    }

    @DisplayName("Should Update Aluguel Details Sucessfully")
    @Test
    void testGivenAluguelObject_whenUpdateAluguel_thenReturnSavedAluguel() {
        //Given
        //BeforeEach
        repository.save(aluguel);
        statusList.add(Aluguel.AluguelStatus.PENDENTE);
        Aluguel foundAluguel = repository.findById(aluguel.getId()).get();

        //When
        foundAluguel.setAluguelStatus(statusList);
        foundAluguel.setDataInicio(LocalDate.of(2026, 05, 20));
        foundAluguel.setDataTermino(LocalDate.of(2026, 05, 30));
        foundAluguel.setUpdatedAt(Instant.now());
        repository.save(foundAluguel);

        //Then
        assertNotNull(foundAluguel);
        assertThat(foundAluguel.getAluguelStatus()).contains(Aluguel.AluguelStatus.PENDENTE, Aluguel.AluguelStatus.ATIVO);
        assertEquals(LocalDate.of(2026, 05, 30), foundAluguel.getDataTermino());
        assertTrue(foundAluguel.getCreatedAt().isBefore(Instant.now()));
    }

    @DisplayName("Should Remove Aluguel From Database")
    @Test
    void testGivenAluguelObject_whenDelete_thenRemoveAluguel() {
        //Given
        //BeforeEach
        repository.save(aluguel);

        //When
        repository.deleteById(aluguel.getId());
        Optional<Aluguel> aluguelOptional = repository.findById(aluguel.getId());

        //Then
        assertTrue(aluguelOptional.isEmpty());
    }

    @DisplayName("Should Find Aluguel By Their Unique ID")
    @Test
    void testGivenAluguelObjectID_whenFindById_thenReturnAluguelObejct() {
        //Given
        //BeforeEach
        repository.save(aluguel);

        //When
        Aluguel foundAluguel = repository.findById(aluguel.getId()).get();

        //Then
        assertNotNull(foundAluguel);
        assertEquals(aluguel.getDataInicio(), foundAluguel.getDataInicio());
        assertEquals(aluguel.getId(), foundAluguel.getId());
    }
}
