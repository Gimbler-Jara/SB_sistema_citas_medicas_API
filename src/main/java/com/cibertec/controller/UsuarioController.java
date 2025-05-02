package com.cibertec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cibertec.dto.LoginDTO;
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

}