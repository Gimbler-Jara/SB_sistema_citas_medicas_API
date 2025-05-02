package com.cibertec.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cibertec.dto.LoginDTO;
import com.cibertec.dto.UsuarioRequestDTO;
import com.cibertec.model.DocumentType;
import com.cibertec.model.Rol;
import com.cibertec.model.Usuario;
import com.cibertec.repository.DocumentTypeRepository;
import com.cibertec.repository.RolRepository;
import com.cibertec.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private RolRepository rolRepository;

	@Autowired
	private DocumentTypeRepository documentTypeRepository;

	private final PasswordEncoder passwordEncoder;

	public UsuarioService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	// MÉTODO PARA LISTAR TODOS LOS USUARIOS
	public ResponseEntity<List<Usuario>> listarUsuarios() {
		List<Usuario> usuarios = usuarioRepository.findAll();
		if (usuarios.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(usuarios);
		}
	}

	// MÉTODO PARA OBTENER UN USUARIO POR ID
	public ResponseEntity<Optional<Usuario>> obtenerUsuarioPorId(Integer id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		if (usuario.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(usuario);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	// MÉTODO PARA AGREGAR UN NUEVO USUARIO
	public ResponseEntity<Usuario> agregarUsuario(UsuarioRequestDTO usuarioRequest) {
		try {

			Usuario usuario = new Usuario();
			usuario.setDni(usuarioRequest.dni);
			usuario.setLastName(usuarioRequest.lastName);
			usuario.setMiddleName(usuarioRequest.middleName);
			usuario.setFirstName(usuarioRequest.firstName);
			usuario.setBirthDate(usuarioRequest.birthDate);
			usuario.setGender(usuarioRequest.gender);
			usuario.setTelefono(usuarioRequest.telefono);
			usuario.setEmail(usuarioRequest.email);
			usuario.setPasswordHash(usuarioRequest.passwordHash);
			usuario.setActivo(true);

			// Aquí es donde conviertes el ID a la entidad
			DocumentType docType = documentTypeRepository.findById(usuarioRequest.document_type)
					.orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado"));
			usuario.setDocumentType(docType);

			Rol rol = rolRepository.findById(usuarioRequest.rol_id)
					.orElseThrow(() -> new RuntimeException("Rol no encontrado"));
			usuario.setRol(rol);
			usuarioRepository.save(usuario);
			return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
		} catch (Exception e) {
			System.out.println("Ocurrió un error inesperado: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// MÉTODO PARA ACTUALIZAR UN USUARIO EXISTENTE
	public ResponseEntity<Usuario> actualizarUsuario(Integer id, Usuario usuario) {
		Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
		if (usuarioExistente.isPresent()) {
			Usuario usr = usuarioExistente.get();
			usr.setDocumentType(usuario.getDocumentType());
			usr.setDni(usuario.getDni());
			usr.setLastName(usuario.getLastName());
			usr.setMiddleName(usuario.getMiddleName());
			usr.setFirstName(usuario.getFirstName());
			usr.setBirthDate(usuario.getBirthDate());
			usr.setGender(usuario.getGender());
			usr.setTelefono(usuario.getTelefono());
			usr.setEmail(usuario.getEmail());
			usr.setPasswordHash(usuario.getPasswordHash());
			usr.setRol(usuario.getRol());
			try {
				usuarioRepository.save(usr);
				return ResponseEntity.status(HttpStatus.CREATED).body(usr);
			} catch (Exception e) {
				System.out.println("Ocurrió un error inesperado: " + e.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	// MÉTODO PARA ELIMINAR UN USUARIO DE MANERA LOGICA
	public ResponseEntity<?> cambiarEstadoUsuario(Integer idUsuario) {
	    Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

	    if (usuarioOpt.isPresent()) {
	        try {
	            Usuario usuario = usuarioOpt.get();

	            boolean estadoActual = usuario.getActivo();
	            usuario.setActivo(!estadoActual);
	            usuarioRepository.save(usuario);

	            String mensaje = estadoActual
	                    ? "Usuario desactivado correctamente"
	                    : "Usuario activado correctamente";

	            return ResponseEntity.ok(Map.of("message", mensaje));
	        } catch (Exception e) {
	            System.out.println("Error al cambiar el estado del usuario: " + e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(Map.of("message", "Error interno al cambiar estado del usuario"));
	        }
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(Map.of("message", "Usuario no encontrado"));
	    }
	}


	public ResponseEntity<?> buscarPorEmail(LoginDTO user) {
		Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(user.email);

	    if (usuarioOpt.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(Map.of("message", "Usuario no encontrado"));
	    }

	    Usuario usuario = usuarioOpt.get();

	    if (!usuario.getActivo()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(Map.of("message", "Usuario inactivo"));
	    }

	    if (!passwordEncoder.matches(user.password, usuario.getPasswordHash())) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(Map.of("message", "Contraseña incorrecta"));
	    }

	    return ResponseEntity.ok(usuario);
	}

}
