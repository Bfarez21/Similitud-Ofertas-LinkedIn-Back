package com.bryan.similitudofertaslinkedinback.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class PreguntaAsignada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JsonBackReference
    private Usuario usuario;

    @ManyToOne
    private BancoPreguntas pregunta;

    private String respuesta; // la respuesta ingresada por el usuario


    public BancoPreguntas getPregunta() {
        return pregunta;
    }

    public void setPregunta(BancoPreguntas pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
