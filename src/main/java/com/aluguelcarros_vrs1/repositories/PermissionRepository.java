package com.aluguelcarros_vrs1.repositories;

import java.util.Optional;

import com.aluguelcarros_vrs1.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByDescription(String description);
}
