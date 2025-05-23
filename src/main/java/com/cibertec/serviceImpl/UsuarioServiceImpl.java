package com.cibertec.serviceImpl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cibertec.dto.LoginDTO;
import com.cibertec.model.Usuario;
import com.cibertec.repository.UsuarioRepository;
import com.cibertec.security.JwtUtil;
import com.cibertec.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private JwtUtil jwtUtil;

	private final PasswordEncoder passwordEncoder;

	public UsuarioServiceImpl(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public ResponseEntity<Map<String, Object>> obtenerUsuarioPorId(Integer idUsuario) {
		Map<String, Object> response = new HashMap<>();
		Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

		if (usuarioOpt.isPresent()) {
			response.put("mensaje", "Usuario encontrado");
			response.put("httpStatus", HttpStatus.OK.value());
			response.put("usuario", usuarioOpt.get());
			return ResponseEntity.ok(response);
		} else {
			response.put("mensaje", "Usuario no encontrado");
			response.put("httpStatus", HttpStatus.NOT_FOUND.value());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> cambiarEstadoUsuario(Integer idUsuario) {
		Map<String, Object> response = new HashMap<>();
		Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

		if (usuarioOpt.isPresent()) {
			try {
				Usuario usuario = usuarioOpt.get();
				boolean estadoActual = usuario.getActivo();
				usuario.setActivo(!estadoActual);
				usuarioRepository.save(usuario);

				String mensaje = estadoActual ? "Usuario desactivado correctamente" : "Usuario activado correctamente";
				response.put("mensaje", mensaje);
				response.put("httpStatus", HttpStatus.OK.value());
				return ResponseEntity.ok(response);
			} catch (Exception e) {
				response.put("mensaje", "Error interno al cambiar estado del usuario");
				response.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			response.put("mensaje", "Usuario no encontrado");
			response.put("httpStatus", HttpStatus.NOT_FOUND.value());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}

	@Override
	public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	@Override
	public ResponseEntity<Map<String, Object>> buscarPorEmail(LoginDTO user) {
		Map<String, Object> response = new HashMap<>();
		Optional<Usuario> usuarioOpt = obtenerUsuarioPorEmail(user.email);

		if (usuarioOpt.isEmpty()) {
			response.put("mensaje", "Usuario no encontrado");
			response.put("httpStatus", HttpStatus.NOT_FOUND.value());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

		Usuario usuario = usuarioOpt.get();

		if (!usuario.getActivo()) {
			response.put("mensaje", "Usuario inactivo");
			response.put("httpStatus", HttpStatus.UNAUTHORIZED.value());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}

		if (!passwordEncoder.matches(user.password, usuario.getPasswordHash())) {
			response.put("mensaje", "Contraseña incorrecta");
			response.put("httpStatus", HttpStatus.UNAUTHORIZED.value());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}

		Map<String, Object> claims = new HashMap<>();
		claims.put("rol", usuario.getRol().getRol());
		claims.put("id", usuario.getId());
		claims.put("nombre", usuario.getFirstName());

		String token = jwtUtil.generarToken(usuario.getEmail(), claims);

		response.put("mensaje", "Login exitoso");
		response.put("httpStatus", HttpStatus.OK.value());
		response.put("usuario", usuario);
		response.put("token", token);

		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Map<String, Object>> refreshToken(String email) {
		Map<String, Object> response = new HashMap<>();

		Usuario usuario = obtenerUsuarioPorEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("No encontrado"));

		Map<String, Object> claims = new HashMap<>();
		claims.put("rol", usuario.getRol().getRol());
		claims.put("id", usuario.getId());
		claims.put("nombre", usuario.getFirstName());

		String nuevoToken = jwtUtil.generarToken(usuario.getEmail(), claims);

		response.put("mensaje", "Token renovado");
		response.put("httpStatus", HttpStatus.OK.value());
		response.put("token", nuevoToken);

		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Map<String, Object>> obtenerMiPerfil(Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		if (authentication == null) {
			response.put("mensaje", "Token expirado o inválido");
			response.put("httpStatus", HttpStatus.UNAUTHORIZED.value());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}

		Optional<Usuario> usuario = obtenerUsuarioPorEmail(authentication.getName());

		if (usuario.isPresent()) {
			response.put("mensaje", "Perfil del usuario");
			response.put("httpStatus", HttpStatus.OK.value());
			response.put("usuario", usuario.get());
			return ResponseEntity.ok(response);
		} else {
			response.put("mensaje", "Usuario no encontrado");
			response.put("httpStatus", HttpStatus.NOT_FOUND.value());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}
}
