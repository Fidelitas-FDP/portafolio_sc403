package com.fdp.portafolio_sc403.repository;

import com.fdp.portafolio_sc403.domain.Rol;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    public Optional<Rol> findByRol(String rol);

}
