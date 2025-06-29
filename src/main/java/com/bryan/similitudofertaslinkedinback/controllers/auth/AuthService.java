package com.bryan.similitudofertaslinkedinback.controllers.auth;


import com.bryan.similitudofertaslinkedinback.jwt.JwtService;
import com.bryan.similitudofertaslinkedinback.model.*;
import com.bryan.similitudofertaslinkedinback.model.auth.AuthResponse;
import com.bryan.similitudofertaslinkedinback.model.auth.LoginRequest;
import com.bryan.similitudofertaslinkedinback.model.auth.RegisterRequest;
import com.bryan.similitudofertaslinkedinback.repository.*;
import org.springframework.security.authentication.AuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // Repositorios que debes inyectar
    private final PreguntaRepository preguntaRepository;
    private final ImageRepository bancoImageRepository;
    private final PreguntaAsignadaRepository preguntaAsignadaRepository;
    private final ImageAsignadaRepository imageAsignadoRepository;

    public AuthResponse login(LoginRequest loginRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        UserDetails userDetails= usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow();
        String token = jwtService.getToken(userDetails);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    // service register
   /* public AuthResponse register(RegisterRequest request){
        Usuario usuario= Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .apellido(request.getApellido())
                .dni(request.getDni())
                .build();
        usuarioRepository.save(usuario);
        return AuthResponse.builder()
                .token(jwtService.getToken(usuario))
                .build();
    }  */

    public AuthResponse register(RegisterRequest request) {
        // 1. Crear un objeto usuario
        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .dni(request.getDni())
                .name(request.getName())
                .apellido(request.getApellido())
                .build();
        usuarioRepository.save(usuario);

        // 2. Seleccionar pregunta e imagen aleatoria del banco por id
        BancoPreguntas pregunta = preguntaRepository.findById(request.getPreguntaId())
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"));

        BancoImage imagen = bancoImageRepository.findById(request.getImagenId())
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        // 3. Crea objetos para asignación y guardamos pregunta y respuesta
        PreguntaAsignada asignada = new PreguntaAsignada();
        asignada.setUsuario(usuario);
        asignada.setPregunta(pregunta);
        asignada.setRespuesta(passwordEncoder.encode(request.getRespuestaSeguridad()));

        ImageAsignada imagenAsignada = new ImageAsignada();
        imagenAsignada.setUsuario(usuario);
        imagenAsignada.setImagenSeleccionada(imagen);

        // 4. Guardar asignaciones entre usuario pregunta y usario imagen
        preguntaAsignadaRepository.save(asignada);
        imageAsignadoRepository.save(imagenAsignada);

        // 5. Crear token
        String jwt = jwtService.getToken(usuario);
        return new AuthResponse(jwt, usuario);
    }


    // Realiza la consulta en la base de datos para verificar si existe un usuario con el mismo correo electrónico
    public boolean isEmailAlreadyRegistered(String email){
        Optional<Usuario> userOptional = usuarioRepository.findByUsername(email);
        return userOptional.isPresent();
    }
}

