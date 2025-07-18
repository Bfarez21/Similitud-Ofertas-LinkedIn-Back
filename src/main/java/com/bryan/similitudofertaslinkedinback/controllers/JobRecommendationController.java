package com.bryan.similitudofertaslinkedinback.controllers;

import com.bryan.similitudofertaslinkedinback.dto.JobRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recomendacion")
public class JobRecommendationController {
    private final String FLASK_URL = "http://localhost:5000/predict";

    @PostMapping("/buscar")
    public ResponseEntity<?> obtenerRecomendaciones(@RequestBody JobRequest request) {
        try {
            // Validar campo requerido
            if (request.getText() == null || request.getText().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El campo 'text' es obligatorio"));
            }

            // Crear payload para Flask
            Map<String, Object> payload = Map.of("text", request.getText());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List> response = restTemplate.postForEntity(FLASK_URL, entity, List.class);

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al comunicarse con Flask: " + e.getMessage()));
        }
    }
    // para verificar
    @GetMapping("/verificar-flask")
    public ResponseEntity<?> verificarFlask() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> respuesta = restTemplate.getForEntity("http://localhost:5000/health", Map.class);
            return ResponseEntity.ok(Map.of("status", "Flask conectado", "response", respuesta.getBody()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "No se puede conectar con Flask"));
        }
    }
}
