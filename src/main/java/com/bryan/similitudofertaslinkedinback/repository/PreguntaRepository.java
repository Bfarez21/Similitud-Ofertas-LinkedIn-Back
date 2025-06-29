package com.bryan.similitudofertaslinkedinback.repository;

import com.bryan.similitudofertaslinkedinback.model.BancoPreguntas;
import com.bryan.similitudofertaslinkedinback.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface PreguntaRepository extends JpaRepository<BancoPreguntas, Long> {
    @Query(value = "SELECT * FROM banco_preguntas ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<BancoPreguntas> findRandomPregunta();
}
