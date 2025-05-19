package com.cibertec.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cibertec.dto.MedicoActualizacionDTO;
import com.cibertec.dto.RegistroMedicoDTO;
import com.cibertec.model.Medico;
import com.cibertec.service.MedicoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

	@Autowired
	private MedicoService medicoService;

	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping
	public ResponseEntity<List<Medico>> listarMedicos() {
		return medicoService.listarMedicos();
	}

	@GetMapping("/{idMedico}")
	public ResponseEntity<Optional<Medico>> obtenerMedico(@PathVariable int idMedico) {
		return medicoService.obtenerMedicoPorId(idMedico);
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> registrarMedico(@RequestPart("datos") String datosJson,
			@RequestPart("archivoFirmaDigital") MultipartFile archivoFirmaDigital) {

		try {
			RegistroMedicoDTO dto = objectMapper.readValue(datosJson, RegistroMedicoDTO.class);
			Medico medico = medicoService.registrarMedico(dto, archivoFirmaDigital);
			return ResponseEntity.ok(medico);
		} catch (JsonProcessingException e) {
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "JSON inválido en 'datos'"));
		} catch (IOException e) {
			return ResponseEntity.internalServerError()
					.body(Map.of("success", false, "message", "Error al registrar médico"));
		}
	}

	@GetMapping("/obtnerFirma")
	public ResponseEntity<?> obtenerUrlFirmaDigital(@RequestParam String path) {
		return medicoService.obtenerUrlFirmaDigital(path);
	}

	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> actualizarMedico(@PathVariable Integer id, @RequestPart("datos") String datosJson,
			@RequestPart(value = "archivoFirmaDigital", required = false) MultipartFile archivoFirmaDigital)
			throws IOException {

		try {
			MedicoActualizacionDTO dto = objectMapper.readValue(datosJson, MedicoActualizacionDTO.class);
			return medicoService.actualizarMedico(id, dto, archivoFirmaDigital);
		} catch (JsonProcessingException e) {
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "JSON inválido en 'datos'"));
		}
	}

	@GetMapping("/especialidad_por_id_medico/{idMedico}")
	public ResponseEntity<?> obtenerEspecialidadPorIdMedico(@PathVariable("idMedico") int idMedico) {
		return medicoService.obtenerEspecialidadPorIdMedico(idMedico);
	}

	@GetMapping("/horario-trabajo-medico/{idMedico}")
	public ResponseEntity<?> listarHorariosDeTrabajoMedico(@PathVariable Integer idMedico) {
		return medicoService.listarHorariosDeTrabajoMedico(idMedico);
	}

	@GetMapping("/medicos-por-especialidad/{idEspecialidad}")
	public ResponseEntity<?> listarMedicosPorEspecialidad(@PathVariable("idEspecialidad") int idEspecialidad) {
		return medicoService.listarMedicosPorEspecialidad(idEspecialidad);
	}

	@GetMapping("/dias-disponibles/{idMedico}")
	public ResponseEntity<?> listarDiasDisponibles(@PathVariable("idMedico") int idMedico) {
		return medicoService.listarDiasDisponiblesPorMedico(idMedico);
	}

	@GetMapping("/horas-disponibles")
	public ResponseEntity<?> listarHorasDisponibles(@RequestParam int idMedico, @RequestParam String fecha) {
		return medicoService.listarHorasDisponibles(idMedico, LocalDate.parse(fecha));
	}

}
