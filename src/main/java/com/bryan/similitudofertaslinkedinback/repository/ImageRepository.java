package com.bryan.similitudofertaslinkedinback.repository;

import com.bryan.similitudofertaslinkedinback.model.BancoImage;
import com.bryan.similitudofertaslinkedinback.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<BancoImage, Long> {
    @Query(value = "SELECT * FROM banco_image ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<BancoImage> findRandomImage();
}
