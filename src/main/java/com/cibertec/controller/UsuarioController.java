package com.cibertec.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import com.cibertec.dto.LoginDTO;
import com.cibertec.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Map<String, Object>> obtenerUsuarioPorId(@PathVariable Integer idUsuario) {
        try {
            return usuarioService.obtenerUsuarioPorId(idUsuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "mensaje", "Error al obtener el usuario",
                "httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value()
            ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDTO user) {
        try {
            return usuarioService.buscarPorEmail(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "mensaje", "Error al intentar iniciar sesión",
                "httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value()
            ));
        }
    }

    @PutMapping("/cambiar-estado-usuario/{id}")
    public ResponseEntity<Map<String, Object>> cambiarEstado(@PathVariable("id") Integer idUsuario) {
        try {
            return usuarioService.cambiarEstadoUsuario(idUsuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "mensaje", "Error al cambiar el estado del usuario",
                "httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value()
            ));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> obtenerMiPerfil(Authentication authentication) {
        try {
            return usuarioService.obtenerMiPerfil(authentication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "mensaje", "Error al obtener el perfil del usuario",
                "httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value()
            ));
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshToken(Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "mensaje", "Token expirado o inválido",
                    "httpStatus", HttpStatus.UNAUTHORIZED.value()
                ));
            }
            return usuarioService.refreshToken(authentication.getName());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "mensaje", "Error al renovar el token",
                "httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value()
            ));
        }
    }
}
