package com.aluguelcarros_vrs1.domain.aluguel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AluguelRepository extends JpaRepository<Aluguel, Long>{
    
    Page<Aluguel> findAllByAtivoTrue(Pageable paginacao);
}
