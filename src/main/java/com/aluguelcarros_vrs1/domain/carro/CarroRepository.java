package com.aluguelcarros_vrs1.domain.carro;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CarroRepository extends JpaRepository<Carro, Long>{
    
    Page<Carro> findAllByAtivoTrue(Pageable paginacao);

    @Query("""
        SELECT c.ativo 
        FROM Carro c 
        WHERE c.id = :id
        """)
    Boolean findAtivoById(Long id);
    

}
