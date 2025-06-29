package com.bryan.similitudofertaslinkedinback.model.auth;

import com.bryan.similitudofertaslinkedinback.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    String token;
    Usuario usuario;
}