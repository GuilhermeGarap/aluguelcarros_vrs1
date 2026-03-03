package com.aluguelcarros_vrs1.repositories;

import java.time.LocalDate;
import java.util.List;

import com.aluguelcarros_vrs1.domain.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {

    @Query(value = "SELECT * FROM alugueis WHERE aluguel_status && CAST(:statusList AS text[])", nativeQuery = true)
    List<Aluguel> findByStatusIn(@Param("statusList") List<String> statusList);

    // Corrigido para Native Query
    @Query(value = "SELECT * FROM alugueis WHERE 'ATIVO' = ANY(aluguel_status) AND data_termino <= :hoje", nativeQuery = true)
    List<Aluguel> findExpirados(@Param("hoje") LocalDate hoje);

    @Query(value = "SELECT * FROM alugueis WHERE aluguel_status && ARRAY['ATIVO', 'PENDENTE']::text[]", nativeQuery = true)
    List<Aluguel> findAtivosAndPendentes();

    @Query(value = "SELECT * FROM alugueis WHERE aluguel_status && ARRAY['INATIVO', 'CANCELADO', 'ENCERRADO']::text[]", nativeQuery = true)
    List<Aluguel> findInativosAndEncerradosAndCancelados();
}
