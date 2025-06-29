package com.bryan.similitudofertaslinkedinback.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "banco_preguntas")
public class BancoPreguntas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPregunta;

    @Column(nullable = false)
    private String texto;

    @Column(nullable = true)
    private String respuesta;

    public String getTexto() {
        return texto;
    }
}
