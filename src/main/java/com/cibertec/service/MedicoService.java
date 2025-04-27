package com.cibertec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
	
	public ResponseEntity<List<Medico>> listarMedicos() {
		List<Medico> medicos = medicoRepository.findAll();
		if (medicos.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(medicos); 
		}
	}
		

	public Medico registrarMedico(RegistroMedicoDTO dto) {

		System.out.println(dto.getDocumentTypeId());

		// 1. Recuperar entidades relacionadas
		DocumentType documentType = documentTypeRepository.findById(dto.getDocumentTypeId())
				.orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado"));
		Rol rol = rolRepository.findById(2) // O el ID de tu rol "Médico"
				.orElseThrow(() -> new RuntimeException("Rol médico no encontrado"));
		Especialidad especialidad = especialidadRepository.findById(dto.getEspecialidadId())
				.orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

		System.out.println("DocumentType ID: " + documentType.getId());
		System.out.println("Rol ID: " + rol.getId());
		System.out.println("Especialidad ID: " + especialidad.getId());

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
		// Debes hashear el password aquí
		usuario.setPasswordHash(dto.getPassword());
		usuario.setRol(rol);

		Usuario usuarioGuardado = usuarioRepository.save(usuario);

		// 3. Crear medico
		Medico medico = new Medico();
		medico.setUsuario(usuarioGuardado);
		medico.setEspecialidad(especialidad);

		return medicoRepository.save(medico);
	}
	
	
}
