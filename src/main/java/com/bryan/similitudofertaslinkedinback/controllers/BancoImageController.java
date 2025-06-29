package com.bryan.similitudofertaslinkedinback.controllers;

import com.bryan.similitudofertaslinkedinback.model.BancoImage;
import com.bryan.similitudofertaslinkedinback.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/banco-image")
public class BancoImageController {

    private final ImageRepository bancoImageRepository;

    @Autowired
    public BancoImageController(ImageRepository bancoImageRepository) {
        this.bancoImageRepository = bancoImageRepository;
    }

    @GetMapping
    public List<BancoImage> listar() {
        return bancoImageRepository.findAll();
    }

    @PostMapping
    public BancoImage crear(@RequestBody BancoImage image) {
        return bancoImageRepository.save(image);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BancoImage> actualizar(@PathVariable Long id, @RequestBody BancoImage nuevoImage) {
        return bancoImageRepository.findById(id)
                .map(image -> {
                    image.setNombre(nuevoImage.getNombre());
                    image.setImagen(nuevoImage.getImagen());
                    return ResponseEntity.ok(bancoImageRepository.save(image));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (bancoImageRepository.existsById(id)) {
            bancoImageRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}