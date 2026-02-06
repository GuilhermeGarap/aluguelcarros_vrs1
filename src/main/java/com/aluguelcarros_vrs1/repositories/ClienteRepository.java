package com.aluguelcarros_vrs1.repositories;

import java.util.List;

import com.aluguelcarros_vrs1.domain.cliente.Cliente;
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
    Boolean existsByCpf(String cpf);
    Boolean existsByNome(String nome);
    Boolean existsByTelefone(String telefone);
    Boolean existsByEmail(String email);
}
