package com.aluguelcarros_vrs1.domain.aluguel;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AluguelRepository extends JpaRepository<Aluguel, Long>{
    
    Page<Aluguel> findAllByAtivoTrue(Pageable paginacao);
    Page<Aluguel> findAllByAtivoFalse(Pageable paginacao);
    List<Aluguel> findAllByAtivoTrue();
}
