package com.cibertec.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cibertec.dto.PacienteActualizacionDTO;
import com.cibertec.dto.RegistroPacienteDTO;
import com.cibertec.model.DocumentType;
import com.cibertec.model.Paciente;
import com.cibertec.model.Rol;
import com.cibertec.model.Usuario;
import com.cibertec.repository.DocumentTypeRepository;
import com.cibertec.repository.PacienteRepository;
import com.cibertec.repository.RolRepository;
import com.cibertec.repository.UsuarioRepository;

@Service
public class PacienteService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PacienteRepository pacienteRepository;

	@Autowired
	private DocumentTypeRepository documentTypeRepository;

	@Autowired
	private RolRepository rolRepository;

	private final PasswordEncoder passwordEncoder;

	public PacienteService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public ResponseEntity<List<Paciente>> listarPacientes() {
		List<Paciente> pacientes = pacienteRepository.findAll();
		if (pacientes.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(pacientes);
		}
	}

	public Paciente registrarPaciente(RegistroPacienteDTO dto) {
		
		if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo electrónico ya está registrado.");
		}

		DocumentType documentType = documentTypeRepository.findById(dto.getDocumentTypeId()).orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado"));
		Rol rol = rolRepository.findById(1).orElseThrow(() -> new RuntimeException("Rol paciente no encontrado"));

		Usuario usuario = new Usuario();
		usuario.setDocumentType(documentType);
		usuario.setDni(dto.getDni());
		usuario.setLastName(dto.getLastName());
		usuario.setMiddleName(dto.getMiddleName());
		usuario.setFirstName(dto.getFirstName());
		usuario.setBirthDate(dto.getBirthDate());
		usuario.setGender(dto.getGender());
		usuario.setTelefono(dto.getTelefono());
		usuario.setEmail(dto.getEmail());
		usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
		usuario.setRol(rol);
		usuario.setActivo(true);
		
		Usuario usuarioGuardado = usuarioRepository.save(usuario);

		Paciente paciente = new Paciente();
		paciente.setUsuario(usuarioGuardado);

		return pacienteRepository.save(paciente);
	}


	public ResponseEntity<?> actualizarPaciente(Integer id, PacienteActualizacionDTO dto) {
		Optional<Paciente> pacienteOpt = pacienteRepository.findById(id);
		if (pacienteOpt.isPresent()) {
			Paciente paciente = pacienteOpt.get();
			Usuario usuario = paciente.getUsuario();
			usuario.setFirstName(dto.getFirstName());
			usuario.setMiddleName(dto.getMiddleName());
			usuario.setLastName(dto.getLastName());
			usuario.setTelefono(dto.getTelefono());
			usuarioRepository.save(usuario);
			return ResponseEntity.ok(Map.of("success", true, "message", "Paciente actualizado correctamente"));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("success", false, "message", "Paciente no encontrado"));
		}
	}

}
