package com.bryan.similitudofertaslinkedinback.repository;

import com.bryan.similitudofertaslinkedinback.model.PreguntaAsignada;
import com.bryan.similitudofertaslinkedinback.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreguntaAsignadaRepository extends JpaRepository<PreguntaAsignada, Long> {
    Optional<PreguntaAsignada> findByUsuario(Usuario usuario);

}
