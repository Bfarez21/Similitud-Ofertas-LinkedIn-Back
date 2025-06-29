package com.bryan.similitudofertaslinkedinback.repository;

import com.bryan.similitudofertaslinkedinback.model.ImageAsignada;
import com.bryan.similitudofertaslinkedinback.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageAsignadaRepository extends JpaRepository<ImageAsignada, Long> {
    Optional<ImageAsignada> findByUsuario(Usuario usuario);

}
