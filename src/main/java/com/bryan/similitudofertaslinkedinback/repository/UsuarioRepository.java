package com.bryan.similitudofertaslinkedinback.repository;


import com.bryan.similitudofertaslinkedinback.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // servicios por atributos lo que quermos encontrar...
    Optional<Usuario> findByUsername(String correo);
}