package com.aluguelcarros_vrs1.domain.carro;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CarroRepositoryTest {

    @Autowired
    CarroRepository repository;

    @Test
    void GivenCarroObject_whenRepositorySave_thenReturnSavedCliente() {
        //Given
        Carro carro = new Carro("Fiat", 25F, 3, true, 2);

        //When
        Carro carroSalvo = repository.save(carro);

        //Then
        assertNotNull(carroSalvo);
        assertEquals(carro.getId(), carroSalvo.getId());
        assertTrue(carroSalvo.getAtivo());
    }

    @Test
    void GivenCarroList_whenFindAllByAtivoTrue_thenReturnCarroListWithOnlyCarrosAtivos() {
        //Given
        Carro carro = new Carro("Fiat", 25F, 3, true, 2);
        Carro carro2 = new Carro("Gol", 15F, 2, false, 1);
        repository.save(carro);
        repository.save(carro2);

        //When
        List<Carro> listaCarros = repository.findAllByAtivoTrue();

        //Then
        assertEquals(1, listaCarros.size());
        assertEquals(true, listaCarros.get(0).getAtivo());
        assertTrue(listaCarros.get(0).getDisponivel() < listaCarros.get(0).getUnidades());
    }

    @Test
    void GivenCarroList_whenFindAll_thenReturnCarroList() {
        //Given
        Carro carro = new Carro("Fiat", 25F, 3, true, 2);
        Carro carro2 = new Carro("Gol", 15F, 2, false, 1);
        repository.save(carro);
        repository.save(carro2);

        //When
        List<Carro> listaCarros = repository.findAll();

        //Then
        assertEquals(2, listaCarros.size());
        assertEquals(true, listaCarros.get(0).getAtivo());
        assertTrue(listaCarros.get(1).getDisponivel() < listaCarros.get(1).getUnidades());
    }

    @Test
    void GivenCarroId_whenFindById_thenReturnCarro() {
        //Given
        Carro carro = new Carro("Fiat", 25F, 3, true, 2);
        repository.save(carro);

        //When
        Carro carroSalvo = repository.findById(carro.getId()).get();

        //Then
        assertNotNull(carroSalvo.getId());
        assertEquals(carro.getId(), carroSalvo.getId());
        assertTrue(carroSalvo.getId() > 0);
    }
}