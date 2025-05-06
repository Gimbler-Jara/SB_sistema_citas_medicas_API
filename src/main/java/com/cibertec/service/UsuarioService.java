package com.cibertec.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cibertec.dto.LoginDTO;
import com.cibertec.model.Usuario;
import com.cibertec.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	private final PasswordEncoder passwordEncoder;

	public UsuarioService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public ResponseEntity<?> cambiarEstadoUsuario(Integer idUsuario) {
		Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

		if (usuarioOpt.isPresent()) {
			try {
				Usuario usuario = usuarioOpt.get();

				boolean estadoActual = usuario.getActivo();
				usuario.setActivo(!estadoActual);
				usuarioRepository.save(usuario);

				String mensaje = estadoActual ? "Usuario desactivado correctamente" : "Usuario activado correctamente";

				return ResponseEntity.ok(Map.of("message", mensaje));
			} catch (Exception e) {
				System.out.println("Error al cambiar el estado del usuario: " + e.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(Map.of("message", "Error interno al cambiar estado del usuario"));
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Usuario no encontrado"));
		}
	}

	public ResponseEntity<?> buscarPorEmail(LoginDTO user) {
		Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(user.email);

		if (usuarioOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Usuario no encontrado"));
		}

		Usuario usuario = usuarioOpt.get();

		if (!usuario.getActivo()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Usuario inactivo"));
		}

		if (!passwordEncoder.matches(user.password, usuario.getPasswordHash())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Contraseña incorrecta"));
		}

		return ResponseEntity.ok(usuario);
	}

}
