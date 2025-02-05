package com.aluguelcarros_vrs1.domain.aluguel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {

    List<Aluguel> findAllByAtivoTrue();  
    List<Aluguel> findAllByAtivoFalse(); 
    List<Aluguel> findAll();             

}
