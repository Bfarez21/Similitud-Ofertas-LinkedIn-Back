package com.bryan.similitudofertaslinkedinback.dto;

import lombok.Data;

@Data
public class VerificacionRequest {
    private String username;
    private String respuesta;
    private Long imagenId; // id de imagen seleccionada por el usuario
}
