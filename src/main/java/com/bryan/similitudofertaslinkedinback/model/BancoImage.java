package com.bryan.similitudofertaslinkedinback.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "banco_image")
public class BancoImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImage;

    private String nombre; // Ej: estrella
    private String imagen; // URL o path al recurso (base64 si prefieres embebido)

    public BancoImage() {

    }
}
