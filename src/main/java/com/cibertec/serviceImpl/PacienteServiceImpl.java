package com.cibertec.serviceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cibertec.dto.PacienteActualizacionDTO;
import com.cibertec.dto.RegistroPacienteDTO;
import com.cibertec.model.*;
import com.cibertec.repository.*;
import com.cibertec.service.PacienteService;

@Service
public class PacienteServiceImpl implements PacienteService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PacienteRepository pacienteRepository;

	@Autowired
	private DocumentTypeRepository documentTypeRepository;

	@Autowired
	private RolRepository rolRepository;

	private final PasswordEncoder passwordEncoder;

	public PacienteServiceImpl(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public ResponseEntity<Map<String, Object>> listarPacientes() {
		Map<String, Object> response = new HashMap<>();
		List<Paciente> pacientes = pacienteRepository.findAll();

		if (pacientes.isEmpty()) {
			response.put("mensaje", "No hay pacientes registrados");
			response.put("httpStatus", HttpStatus.NO_CONTENT.value());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
		}

		response.put("mensaje", "Lista de pacientes");
		response.put("httpStatus", HttpStatus.OK.value());
		response.put("pacientes", pacientes);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Map<String, Object>> registrarPaciente(RegistroPacienteDTO dto) {
	    Map<String, Object> response = new HashMap<>();
	    
	    System.out.println("dtao paciente " + dto.toString());

	    if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
	        response.put("mensaje", "El correo electrónico ya está registrado.");
	        response.put("httpStatus", HttpStatus.BAD_REQUEST.value());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }

	    DocumentType documentType = documentTypeRepository.findById(dto.getDocumentTypeId())
	            .orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado"));

	    Rol rol = rolRepository.findById(1)
	            .orElseThrow(() -> new RuntimeException("Rol paciente no encontrado"));

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

	    Paciente pacienteGuardado = pacienteRepository.save(paciente);

	    response.put("mensaje", "Paciente registrado correctamente");
	    response.put("httpStatus", HttpStatus.OK.value());
	    response.put("paciente", pacienteGuardado);
	    return ResponseEntity.ok(response);
	}


	@Override
	public ResponseEntity<Map<String, Object>> actualizarPaciente(Integer id, PacienteActualizacionDTO dto) {
		Map<String, Object> response = new HashMap<>();
		Optional<Paciente> pacienteOpt = pacienteRepository.findById(id);

		if (pacienteOpt.isEmpty()) {
			response.put("mensaje", "Paciente no encontrado");
			response.put("httpStatus", HttpStatus.NOT_FOUND.value());
			response.put("success", false);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

		Paciente paciente = pacienteOpt.get();
		Usuario usuario = paciente.getUsuario();

		if (dto.getFirstName() != null)
			usuario.setFirstName(dto.getFirstName());
		if (dto.getMiddleName() != null)
			usuario.setMiddleName(dto.getMiddleName());
		if (dto.getLastName() != null)
			usuario.setLastName(dto.getLastName());
		if (dto.getTelefono() != null)
			usuario.setTelefono(dto.getTelefono());
		if (dto.getGender() != null)
			usuario.setGender(dto.getGender());
		if (dto.getDni() != null)
			usuario.setDni(dto.getDni());
		if (dto.getEmail() != null)
			usuario.setEmail(dto.getEmail());

		if (dto.getBirthDate() != null && !dto.getBirthDate().isBlank()) {
			try {
				LocalDate fecha = LocalDate.parse(dto.getBirthDate());
				usuario.setBirthDate(fecha);
			} catch (DateTimeParseException e) {
				response.put("mensaje", "Fecha de nacimiento inválida");
				response.put("httpStatus", HttpStatus.BAD_REQUEST.value());
				response.put("success", false);
				return ResponseEntity.badRequest().body(response);
			}
		}

		if (dto.getDocumentTypeId() != null) {
			DocumentType tipoDoc = documentTypeRepository.findById(dto.getDocumentTypeId()).orElseThrow(
					() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de documento inválido"));
			usuario.setDocumentType(tipoDoc);
		}

		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
		}

		usuarioRepository.save(usuario);

		response.put("mensaje", "Paciente actualizado correctamente");
		response.put("httpStatus", HttpStatus.OK.value());
		response.put("success", true);
		return ResponseEntity.ok(response);
	}
}
