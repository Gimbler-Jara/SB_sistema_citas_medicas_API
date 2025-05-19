package com.cibertec.service;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;
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

	@Autowired
	private FirebaseStorageService firebaseStorageService;

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

	public ResponseEntity<Optional<Medico>> obtenerMedicoPorId(Integer idUsuario) {
		Optional<Medico> medicos = medicoRepository.findById(idUsuario);
		if (medicos.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(medicos);
		}
	}

	public Medico registrarMedico(RegistroMedicoDTO dto, MultipartFile archivoFirmaDigital) throws IOException {

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
		medico.setCmp(dto.getCmp());

		if (archivoFirmaDigital != null && !archivoFirmaDigital.isEmpty()) {
			FirebaseStorageService.FirebaseUploadResult firma = firebaseStorageService.uploadFile(archivoFirmaDigital,
					usuarioGuardado.getId());
			medico.setUrlFirmaDigital(firma.path());
		}

		return medicoRepository.save(medico);
	}

	public ResponseEntity<?> actualizarMedico(Integer id, MedicoActualizacionDTO dto, MultipartFile archivoFirmaDigital)
			throws IOException {
		Optional<Medico> medicoOpt = medicoRepository.findById(id);
		if (medicoOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("success", false, "message", "Médico no encontrado"));
		}

		Medico medico = medicoOpt.get();
		Usuario usuario = medico.getUsuario();

		// Actualizar campos del usuario si no son nulos
		if (dto.getFirstName() != null)
			usuario.setFirstName(dto.getFirstName());
		if (dto.getMiddleName() != null)
			usuario.setMiddleName(dto.getMiddleName());
		if (dto.getLastName() != null)
			usuario.setLastName(dto.getLastName());
		if (dto.getTelefono() != null)
			usuario.setTelefono(dto.getTelefono());
		if (dto.getBirthDate() != null)
			usuario.setBirthDate(dto.getBirthDate());
		if (dto.getGender() != null)
			usuario.setGender(dto.getGender());
		if (dto.getDni() != null)
			usuario.setDni(dto.getDni());
		if (dto.getEmail() != null)
			usuario.setEmail(dto.getEmail());

		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
		}

		if (dto.getDocumentTypeId() != null) {
			DocumentType tipoDoc = documentTypeRepository.findById(dto.getDocumentTypeId()).orElseThrow(
					() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de documento inválido"));
			usuario.setDocumentType(tipoDoc);
		}

		if (dto.getEspecialidadId() != null) {
			Especialidad especialidad = especialidadRepository.findById(dto.getEspecialidadId())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Especialidad no válida"));
			medico.setEspecialidad(especialidad);
		}

		// Reemplazar firma digital si se recibe
		if (archivoFirmaDigital != null && !archivoFirmaDigital.isEmpty()) {
			// Eliminar firma anterior si existía
			if (medico.getUrlFirmaDigital() != null && !medico.getUrlFirmaDigital().isBlank()) {
				// Convertir URL pública a path (inverso a getPublicUrl)
				String urlAnterior = medico.getUrlFirmaDigital();
				String prefix = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/",
						firebaseStorageService.getBucketName());
				if (urlAnterior.startsWith(prefix)) {
					String path = urlAnterior.substring(prefix.length(), urlAnterior.indexOf("?alt=media"));
					firebaseStorageService.deleteFile(path);
				}
			}

			FirebaseStorageService.FirebaseUploadResult firma = firebaseStorageService.uploadFile(archivoFirmaDigital,
					usuario.getId());
			medico.setUrlFirmaDigital(firma.path()); 
		}

		if (dto.getCmp() != null) {
			medico.setCmp(dto.getCmp());
		}

		usuarioRepository.save(usuario);
		medicoRepository.save(medico);

		return ResponseEntity.ok(Map.of("success", true, "message", "Médico actualizado correctamente"));
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

	public ResponseEntity<?> obtenerUrlFirmaDigital(String urlStorage) {
		String url = firebaseStorageService.getPublicUrl(urlStorage);

		if (url.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(url);
		}
	}

}
