package com.cibertec.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cibertec.dto.DiasDisponiblesPorMedicoDTO;
import com.cibertec.dto.DisponibilidadCitaPorMedicoDTO;
import com.cibertec.dto.HorasDispiniblesDeCitasDTO;
import com.cibertec.dto.MedicoActualizacionDTO;
import com.cibertec.dto.MedicosPorEspecialidadDTO;
import com.cibertec.dto.RegistroMedicoDTO;
import com.cibertec.model.DocumentType;
import com.cibertec.model.Especialidad;
import com.cibertec.model.Medico;
import com.cibertec.model.Rol;
import com.cibertec.model.Usuario;
import com.cibertec.repository.DocumentTypeRepository;
import com.cibertec.repository.EspecialidadRepository;
import com.cibertec.repository.MedicoRepository;
import com.cibertec.repository.RolRepository;
import com.cibertec.repository.UsuarioRepository;

@Service
public class MedicoService {
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private MedicoRepository medicoRepository;
	@Autowired
	private DocumentTypeRepository documentTypeRepository;
	@Autowired
	private RolRepository rolRepository;
	@Autowired
	private EspecialidadRepository especialidadRepository;

	private final PasswordEncoder passwordEncoder;

	public MedicoService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public ResponseEntity<List<Medico>> listarMedicos() {
		List<Medico> medicos = medicoRepository.findAll();
		if (medicos.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(medicos);
		}
	}

	public Medico registrarMedico(RegistroMedicoDTO dto) {

		// 0. Verificar si ya existe un usuario con ese email
		if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo electrónico ya está registrado.");
		}

		// 1. Recuperar entidades relacionadas
		DocumentType documentType = documentTypeRepository.findById(dto.getDocumentTypeId())
				.orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado"));
		Rol rol = rolRepository.findById(2).orElseThrow(() -> new RuntimeException("Rol médico no encontrado"));
		Especialidad especialidad = especialidadRepository.findById(dto.getEspecialidadId())
				.orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

		// 2. Crear usuario
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
		usuario.setActivo(true);
		// Hashear la contraseña antes de guardar
		usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
		usuario.setRol(rol);

		Usuario usuarioGuardado = usuarioRepository.save(usuario);

		// 3. Crear medico
		Medico medico = new Medico();
		medico.setUsuario(usuarioGuardado);
		medico.setEspecialidad(especialidad);

		return medicoRepository.save(medico);
	}

	public ResponseEntity<?> actualizarMedico(Integer id, MedicoActualizacionDTO dto) {
		Optional<Medico> medicoOpt = medicoRepository.findById(id);
		if (medicoOpt.isPresent()) {
			Medico medico = medicoOpt.get();
			Usuario usuario = medico.getUsuario();
			usuario.setFirstName(dto.getFirstName());
			usuario.setMiddleName(dto.getMiddleName());
			usuario.setLastName(dto.getLastName());
			usuario.setTelefono(dto.getTelefono());
			usuario.setBirthDate(dto.getBirthDate());
			usuario.setGender(dto.getGender());
			if (dto.getEspecialidadId() != null) {
				Especialidad esp = especialidadRepository.findById(dto.getEspecialidadId()).orElse(null);
				medico.setEspecialidad(esp);
			}
			usuarioRepository.save(usuario);
			medicoRepository.save(medico); // Guarda cambios de especialidad si aplica
			return ResponseEntity.ok(Map.of("success", true, "message", "Médico actualizado correctamente"));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("success", false, "message", "Médico no encontrado"));
		}
	}


	public ResponseEntity<Especialidad> obtenerEspecialidadPorIdMedico(int idMedico) {
		Especialidad especialidad = medicoRepository.obtenerEspecialidadPorIdMedico(idMedico);

		if (especialidad == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(especialidad);
		}
	}

	public ResponseEntity<List<MedicosPorEspecialidadDTO>> listarMedicosPorEspecialidad(int idEspecialidad) {
		List<MedicosPorEspecialidadDTO> medicos = medicoRepository.listarMedicosPorEspecialidad(idEspecialidad);
		if (medicos.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(medicos);
		}
	}

	// 6. Listar días disponibles por médico
	public ResponseEntity<List<DiasDisponiblesPorMedicoDTO>> listarDiasDisponiblesPorMedico(int idMedico) {
		List<DiasDisponiblesPorMedicoDTO> dias = medicoRepository.listarDiasDisponiblesPorMedico(idMedico);
		if (dias.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(dias);
		}
	}

	// 7. Listar horas disponibles
	public ResponseEntity<List<HorasDispiniblesDeCitasDTO>> listarHorasDisponibles(int idMedico, LocalDate fecha) {
		List<HorasDispiniblesDeCitasDTO> horas = medicoRepository.listarHorasDisponibles(idMedico,
				java.sql.Date.valueOf(fecha));
		if (horas.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(horas);
		}
	}

	public ResponseEntity<Map<String, Object>> listarHorariosDeTrabajoMedico(Integer idMedico) {
		List<DisponibilidadCitaPorMedicoDTO> lista = medicoRepository.listarHorariosDeTrabajoMedico(idMedico);
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Disponibilidades encontradas");
		response.put("data", lista);
		return ResponseEntity.ok(response);
	}

}
