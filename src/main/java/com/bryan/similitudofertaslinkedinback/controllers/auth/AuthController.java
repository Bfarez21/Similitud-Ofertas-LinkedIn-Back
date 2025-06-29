package com.bryan.similitudofertaslinkedinback.controllers.auth;


import com.bryan.similitudofertaslinkedinback.dto.PreguntaImagenResponse;
import com.bryan.similitudofertaslinkedinback.dto.VerificacionRequest;
import com.bryan.similitudofertaslinkedinback.model.*;
import com.bryan.similitudofertaslinkedinback.model.auth.AuthResponse;
import com.bryan.similitudofertaslinkedinback.model.auth.LoginRequest;
import com.bryan.similitudofertaslinkedinback.model.auth.RegisterRequest;
import com.bryan.similitudofertaslinkedinback.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Repositorios que debes inyectar
    private final PreguntaRepository preguntaRepository;
    private final ImageRepository bancoImageRepository;
    private final PreguntaAsignadaRepository preguntaAsignadaRepository;
    private final ImageAsignadaRepository imageAsignadoRepository;
    private final ImageRepository imageRepository;


    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest loginRequest){
        AuthResponse authResponse = authService.login(loginRequest);
        Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(()-> new RuntimeException("Usuario not found"));
        authResponse.setUsuario(usuario);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    private ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        if (authService.isEmailAlreadyRegistered(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Correo"+ request.getUsername() + "ya registrado",null));
        }
        return ResponseEntity.ok(authService.register(request));
    }

    // obtener preguntas/image
    @GetMapping("/pregunta-imagen-aleatoria")
    public ResponseEntity<PreguntaImagenResponse> getPreguntaImagenAleatoria() {
        BancoPreguntas pregunta = preguntaRepository.findRandomPregunta()
                .orElseThrow(() -> new RuntimeException("No hay preguntas disponibles"));

        BancoImage imagen = imageRepository.findRandomImage()
                .orElseThrow(() -> new RuntimeException("No hay imágenes disponibles"));

        PreguntaImagenResponse response = new PreguntaImagenResponse(
                pregunta.getIdPregunta(),
                pregunta.getTexto(),
                imagen.getIdImage(),
                imagen.getImagen()
        );

        return ResponseEntity.ok(response);
    }

    // obtener pregunta user
    @GetMapping("/pregunta-asignada-usuario")
    public ResponseEntity<?> obtenerPreguntaDesdeToken(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (usuario.getPreguntaAsignada() == null || usuario.getPreguntaAsignada().getPregunta() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "El usuario no tiene una pregunta asignada"));
        }

        String preguntaTexto = usuario.getPreguntaAsignada()                  // obtiene la entidad PreguntaAsignada del usuario
                                      .getPregunta()                         // obtiene la entidad BancoPreguntas
                                      .getTexto();                           // obtiene el texto real de la pregunta
        return ResponseEntity.ok(Map.of("pregunta", preguntaTexto));
    }


    // verifiacion secundaria
    @PostMapping("/verificacion-secundaria")
    public ResponseEntity<?> verificarPreguntaImagen(@RequestBody VerificacionRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        PreguntaAsignada pregunta = preguntaAsignadaRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Pregunta no asignada"));

        ImageAsignada imagen = imageAsignadoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Imagen no asignada"));

        boolean respuestaOk = passwordEncoder.matches(request.getRespuesta(), pregunta.getRespuesta());
        boolean imagenOk = imagen.getImagenSeleccionada().getIdImage().equals(request.getImagenId());

        if (!respuestaOk && !imagenOk) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Fallo en verificación: respuesta e imagen incorrectas");
        }

        if (!respuestaOk) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Fallo en verificación: respuesta incorrecta");
        }

        if (!imagenOk) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Fallo en verificación: imagen incorrecta");
        }

        return ResponseEntity.ok("Verificación completada");
    }

}
