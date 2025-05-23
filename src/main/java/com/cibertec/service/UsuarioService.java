package com.cibertec.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.cibertec.dto.LoginDTO;
import com.cibertec.model.Usuario;

public interface UsuarioService {

	ResponseEntity<Map<String, Object>> obtenerUsuarioPorId(Integer idUsuario);

	ResponseEntity<Map<String, Object>> cambiarEstadoUsuario(Integer idUsuario);

	ResponseEntity<Map<String, Object>> buscarPorEmail(LoginDTO user);

	ResponseEntity<Map<String, Object>> refreshToken(String email);

	Optional<Usuario> obtenerUsuarioPorEmail(String email);

	ResponseEntity<Map<String, Object>> obtenerMiPerfil(Authentication authentication);
}
