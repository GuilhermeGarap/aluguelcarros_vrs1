package com.aluguelcarros_vrs1.domain.usuario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Test
    void testGivenUsuarioObject_whenSave_thenReturnUsuarioSaved() {
        //Given
        Usuario usuario = new Usuario("roberto@gmail.com", "r12345");

        //When
        Usuario usuarioSalvo = repository.save(usuario);

        //Then
        assertNotNull(usuarioSalvo);
        assertEquals(usuario.getUsername(), usuarioSalvo.getUsername());
    }

    @Test
    void testGivenUsuarioList_whenFindAll_thenReturnUsuarioList() {
        //Given
        Usuario usuario = new Usuario("roberto@gmail.com", "r12345");
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
        Usuario usuario = new Usuario("roberto@gmail.com", "r12345");
        repository.save(usuario);

        //When
        var usuarioSalvo = repository.findBylogin(usuario.getUsername());

        //Then
        assertNotNull(usuarioSalvo);
        assertEquals("roberto@gmail.com", usuarioSalvo.getUsername());
    }
}