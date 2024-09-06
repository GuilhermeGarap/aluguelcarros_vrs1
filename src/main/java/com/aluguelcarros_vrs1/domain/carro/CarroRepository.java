package com.aluguelcarros_vrs1.domain.carro;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarroRepository extends JpaRepository<Carro, Long>{
    
    Page<Carro> findAllByAtivoTrue(Pageable paginacao);
}
