package com.aluguelcarros_vrs1.domain.carro;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CarroRepository extends JpaRepository<Carro, Long> {

    List<Carro> findAllByAtivoTrue();
    
    @Query("""
        SELECT c.ativo 
        FROM Carro c 
        WHERE c.id = :id
        """)
    Boolean findAtivoById(Long id);

}

