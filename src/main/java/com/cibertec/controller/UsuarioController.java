package com.cibertec.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import com.cibertec.dto.LoginDTO;
import com.cibertec.model.Usuario;
import com.cibertec.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;


	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody LoginDTO user) {
		return usuarioService.buscarPorEmail(user); 
	}
	
	
	@PutMapping("cambiar-estado-usuario/{id}")
	public ResponseEntity<?> cambiarEstado(@PathVariable("id") Integer idUsuario) {
	    return usuarioService.cambiarEstadoUsuario(idUsuario);
	}
	
	@GetMapping("/me")
	public ResponseEntity<?> obtenerMiPerfil(Authentication authentication) {
	    String email = authentication.getName();
	    Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorEmail(email);
	    return ResponseEntity.ok(usuario);
	}
	
	@GetMapping("/refresh")
	public ResponseEntity<?> refreshToken(Authentication authentication) {
		if (authentication == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expirado");
	    }
	    return usuarioService.refreshToken(authentication.getName());
	}

}