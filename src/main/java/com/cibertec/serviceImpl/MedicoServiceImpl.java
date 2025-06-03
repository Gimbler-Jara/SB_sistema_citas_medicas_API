package com.cibertec.serviceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.cibertec.dto.*;
import com.cibertec.model.*;
import com.cibertec.repository.*;
import com.cibertec.service.MedicoService;

@Service
public class MedicoServiceImpl implements MedicoService {

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

	public MedicoServiceImpl(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public ResponseEntity<Map<String, Object>> listarMedicos() {
		Map<String, Object> response = new HashMap<>();
		List<Medico> medicos = medicoRepository.findAll();

		if (medicos.isEmpty()) {
			response.put("mensaje", "No hay médicos registrados");
			response.put("httpStatus", HttpStatus.NO_CONTENT.value());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
		}

		response.put("mensaje", "Lista de médicos");
		response.put("httpStatus", HttpStatus.OK.value());
		response.put("medicos", medicos);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Map<String, Object>> obtenerMedicoPorId(Integer idUsuario) {
		Map<String, Object> response = new HashMap<>();
		Optional<Medico> medicoOpt = medicoRepository.findById(idUsuario);

		if (medicoOpt.isEmpty()) { 
			response.put("mensaje", "Médico no encontrado");
			response.put("httpStatus", HttpStatus.NOT_FOUND.value());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

		response.put("mensaje", "Médico encontrado");
		response.put("httpStatus", HttpStatus.OK.value());
		response.put("medico", medicoOpt.get());
		return ResponseEntity.ok(response);
	}

	@Override
	public Medico registrarMedico(RegistroMedicoDTO dto, MultipartFile archivoFirmaDigital) throws IOException {
		
		System.out.println("dtao medico" + dto.toString());
		System.out.println("Archivo " + archivoFirmaDigital);
		
		if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo electrónico ya está registrado.");
		}

		DocumentType documentType = documentTypeRepository.findById(dto.getDocumentTypeId())
				.orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado"));

		Rol rol = rolRepository.findById(2).orElseThrow(() -> new RuntimeException("Rol médico no encontrado"));

		Especialidad especialidad = especialidadRepository.findById(dto.getEspecialidadId())
				.orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

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
		usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
		usuario.setRol(rol);

		Usuario usuarioGuardado = usuarioRepository.save(usuario);

		Medico medico = new Medico();
		medico.setUsuario(usuarioGuardado);
		medico.setEspecialidad(especialidad);
		medico.setCmp(dto.getCmp());

		if (archivoFirmaDigital != null && !archivoFirmaDigital.isEmpty()) {
			FirebaseStorageService.FirebaseUploadResult firma = firebaseStorageService.uploadFile(archivoFirmaDigital,
					usuarioGuardado.getId());
			medico.setUrlFirmaDigital(firma.path());
			System.out.println(firma.path());
		}

		return medicoRepository.save(medico);
	}

	@Override
	public ResponseEntity<Map<String, Object>> actualizarMedico(Integer id, MedicoActualizacionDTO dto,
			MultipartFile archivoFirmaDigital) throws IOException {
		Map<String, Object> response = new HashMap<>();
		Optional<Medico> medicoOpt = medicoRepository.findById(id);

		if (medicoOpt.isEmpty()) {
			response.put("mensaje", "Médico no encontrado");
			response.put("httpStatus", HttpStatus.NOT_FOUND.value());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

		Medico medico = medicoOpt.get();
		Usuario usuario = medico.getUsuario();

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

		if (archivoFirmaDigital != null && !archivoFirmaDigital.isEmpty()) {
			if (medico.getUrlFirmaDigital() != null && !medico.getUrlFirmaDigital().isBlank()) {
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

		if (dto.getCmp() != null)
			medico.setCmp(dto.getCmp());

		usuarioRepository.save(usuario);
		medicoRepository.save(medico);

		response.put("mensaje", "Médico actualizado correctamente");
		response.put("httpStatus", HttpStatus.OK.value());
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Map<String, Object>> obtenerEspecialidadPorIdMedico(int idMedico) {
		Map<String, Object> response = new HashMap<>();
		Especialidad especialidad = medicoRepository.obtenerEspecialidadPorIdMedico(idMedico);

		if (especialidad == null) {
			response.put("mensaje", "Especialidad no encontrada para este médico");
			response.put("httpStatus", HttpStatus.NO_CONTENT.value());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
		}

		response.put("mensaje", "Especialidad encontrada");
		response.put("httpStatus", HttpStatus.OK.value());
		response.put("especialidad", especialidad);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Map<String, Object>> listarMedicosPorEspecialidad(int idEspecialidad) {
		Map<String, Object> response = new HashMap<>();
		List<MedicosPorEspecialidadDTO> medicos = medicoRepository.listarMedicosPorEspecialidad(idEspecialidad);

		if (medicos.isEmpty()) {
			response.put("mensaje", "No hay médicos para esta especialidad");
			response.put("httpStatus", HttpStatus.NO_CONTENT.value());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
		}

		response.put("mensaje", "Lista de médicos por especialidad");
		response.put("httpStatus", HttpStatus.OK.value());
		response.put("medicos", medicos);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Map<String, Object>> listarDiasDisponiblesPorMedico(int idMedico) {
		Map<String, Object> response = new HashMap<>();
		List<DiasDisponiblesPorMedicoDTO> dias = medicoRepository.listarDiasDisponiblesPorMedico(idMedico);

		if (dias.isEmpty()) {
			response.put("mensaje", "No hay días disponibles");
			response.put("httpStatus", HttpStatus.NO_CONTENT.value());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
		}

		response.put("mensaje", "Días disponibles");
		response.put("httpStatus", HttpStatus.OK.value());
		response.put("diasSemana", dias);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Map<String, Object>> listarHorasDisponibles(int idMedico, LocalDate fecha) {
		Map<String, Object> response = new HashMap<>();
		List<HorasDispiniblesDeCitasDTO> horas = medicoRepository.listarHorasDisponibles(idMedico,
				java.sql.Date.valueOf(fecha));

		if (horas.isEmpty()) {
			response.put("mensaje", "No hay horas disponibles para esta fecha");
			response.put("httpStatus", HttpStatus.NO_CONTENT.value());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
		}

		response.put("mensaje", "Horas disponibles");
		response.put("httpStatus", HttpStatus.OK.value());
		response.put("horas", horas);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Map<String, Object>> listarHorariosDeTrabajoMedico(Integer idMedico) {
		Map<String, Object> response = new HashMap<>();
		List<DisponibilidadCitaPorMedicoDTO> lista = medicoRepository.listarHorariosDeTrabajoMedico(idMedico);

		if (lista.isEmpty()) {
			response.put("mensaje", "No cuenta con regitro en el horario");
			response.put("httpStatus", HttpStatus.NO_CONTENT.value());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
		} else {
			response.put("mensaje", "Horarios de trabajo encontrados");
			response.put("httpStatus", HttpStatus.OK.value());
			response.put("datos", lista);
			return ResponseEntity.ok(response);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> obtenerUrlFirmaDigital(String urlStorage) {
		Map<String, Object> response = new HashMap<>();
		String url = firebaseStorageService.getPublicUrl(urlStorage);

		if (url.isEmpty()) {
			response.put("mensaje", "No se encontró la firma digital");
			response.put("httpStatus", HttpStatus.NO_CONTENT.value());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
		}

		response.put("mensaje", "URL obtenida");
		response.put("httpStatus", HttpStatus.OK.value());
		response.put("url", url);
		return ResponseEntity.ok(response);
	}
}
