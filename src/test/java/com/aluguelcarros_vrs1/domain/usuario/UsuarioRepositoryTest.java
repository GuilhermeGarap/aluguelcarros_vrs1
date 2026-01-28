package com.aluguelcarros_vrs1.domain.usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario("roberto@gmail.com", "r12345");
    }

    @Test
    void testGivenUsuarioObject_whenSave_thenReturnUsuarioSaved() {
        //Given

        //When
        Usuario usuarioSalvo = repository.save(usuario);

        //Then
        assertNotNull(usuarioSalvo);
        assertEquals(usuario.getUsername(), usuarioSalvo.getUsername());
    }

    @Test
    void testGivenUsuarioList_whenFindAll_thenReturnUsuarioList() {
        //Given
        Usuario usuario2 = new Usuario("robertos@gmail.com", "r123456");
        repository.save(usuario);
        repository.save(usuario2);

        //When
        List<Usuario> listaUsuarios = repository.findAll();

        //Then
        assertNotNull(listaUsuarios);
        assertEquals(2, listaUsuarios.size());
    }

    @Test
    void testGivenUsuarioUsername_whenFindByUsername_thenReturnUsuario() {
        //Given
        repository.save(usuario);

        //When
        var usuarioSalvo = repository.findBylogin(usuario.getUsername());

        //Then
        assertNotNull(usuarioSalvo);
        assertEquals("roberto@gmail.com", usuarioSalvo.getUsername());
    }
}