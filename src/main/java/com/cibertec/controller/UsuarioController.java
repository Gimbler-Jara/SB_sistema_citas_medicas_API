package com.cibertec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cibertec.dto.LoginDTO;
import com.cibertec.model.Usuario;
import com.cibertec.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;


	@PostMapping("login")
	public ResponseEntity<Usuario> login(@RequestBody LoginDTO user) {
		return usuarioService.buscarPorEmail(user);
	}
}