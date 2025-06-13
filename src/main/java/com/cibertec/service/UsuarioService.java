package com.cibertec.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.cibertec.dto.LoginDTO;
import com.cibertec.model.Usuario;

import jakarta.servlet.http.HttpServletRequest;

public interface UsuarioService {

	ResponseEntity<Map<String, Object>> obtenerUsuarioPorId(Integer idUsuario);

	ResponseEntity<Map<String, Object>> cambiarEstadoUsuario(Integer idUsuario);

	ResponseEntity<Map<String, Object>> login(LoginDTO user);

	Optional<Usuario> obtenerUsuarioPorEmail(String email);

	ResponseEntity<Map<String, Object>> obtenerMiPerfil(Authentication authentication);
	
	ResponseEntity<Map<String, Object>> obtenerDatosPorToken(HttpServletRequest request);
}
