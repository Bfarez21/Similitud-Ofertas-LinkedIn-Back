package com.bryan.similitudofertaslinkedinback.controllers;

import com.bryan.similitudofertaslinkedinback.model.BancoPreguntas;
import com.bryan.similitudofertaslinkedinback.repository.PreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/banco-preguntas")
public class BancoPreguntaController {
    @Autowired
    private PreguntaRepository bancoPreguntaRepository;

    @GetMapping
    public List<BancoPreguntas> listar() {
        return bancoPreguntaRepository.findAll();
    }

    @PostMapping
    public BancoPreguntas crear(@RequestBody BancoPreguntas pregunta) {
        return bancoPreguntaRepository.save(pregunta);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<?>  actualizar(@PathVariable Long id, @RequestBody BancoPreguntas nuevaPregunta) {
        return bancoPreguntaRepository.findById(id)
                .map(pregunta -> {
                    pregunta.setTexto(nuevaPregunta.getTexto());
                    pregunta.setRespuesta(nuevaPregunta.getRespuesta());
                    return ResponseEntity.ok(bancoPreguntaRepository.save(pregunta));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (bancoPreguntaRepository.existsById(id)) {
            bancoPreguntaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
