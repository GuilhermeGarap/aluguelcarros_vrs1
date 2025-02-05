package com.aluguelcarros_vrs1.domain.cliente;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findAllByAtivoTrue();

    @Query("""
        SELECT c.ativo 
        FROM Cliente c 
        WHERE c.id = :id
    """)
    Boolean findAtivoById(Long id);
}
