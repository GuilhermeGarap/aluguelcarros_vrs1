package com.aluguelcarros_vrs1.repositories;

import com.aluguelcarros_vrs1.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    UserDetails findBylogin(String login);
    
}
