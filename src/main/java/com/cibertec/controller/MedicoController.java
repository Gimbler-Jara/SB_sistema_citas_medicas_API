package com.cibertec.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cibertec.dto.MedicoActualizacionDTO;
import com.cibertec.dto.RegistroMedicoDTO;
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
	public ResponseEntity<Map<String, Object>> listarMedicos() {
		try {
			return medicoService.listarMedicos();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error al listar médicos", "httpStatus", 500));
		}
	}

	@GetMapping("/{idMedico}")
	public ResponseEntity<Map<String, Object>> obtenerMedico(@PathVariable int idMedico) {
		try {
			return medicoService.obtenerMedicoPorId(idMedico);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error al obtener el médico", "httpStatus", 500));
		}
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> registrarMedico(@RequestPart("datos") String datosJson,
			@RequestPart("archivoFirmaDigital") MultipartFile archivoFirmaDigital) {

		try {
			RegistroMedicoDTO dto = objectMapper.readValue(datosJson, RegistroMedicoDTO.class);
			return ResponseEntity.ok(Map.of("mensaje", "Médico registrado correctamente", "httpStatus", 200, "datos",medicoService.registrarMedico(dto, archivoFirmaDigital)));
		} catch (JsonProcessingException e) {
			return ResponseEntity.badRequest().body(Map.of("mensaje", "JSON inválido en 'datos'", "httpStatus", 400));
		} catch (IOException e) {
			return ResponseEntity.internalServerError()
					.body(Map.of("mensaje", "Error al registrar médico", "httpStatus", 500));
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(Map.of("mensaje", "Error inesperado", "httpStatus", 500));
		}
	}

	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> actualizarMedico(@PathVariable Integer id,
			@RequestPart("datos") String datosJson,
			@RequestPart(value = "archivoFirmaDigital", required = false) MultipartFile archivoFirmaDigital) {

		try {
			MedicoActualizacionDTO dto = objectMapper.readValue(datosJson, MedicoActualizacionDTO.class);
			return medicoService.actualizarMedico(id, dto, archivoFirmaDigital);
		} catch (JsonProcessingException e) {
			return ResponseEntity.badRequest().body(Map.of("mensaje", "JSON inválido en 'datos'", "httpStatus", 400));
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error al actualizar médico", "httpStatus", 500));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error inesperado", "httpStatus", 500));
		}
	}

	@GetMapping("/obtnerFirma")
	public ResponseEntity<Map<String, Object>> obtenerUrlFirmaDigital(@RequestParam String path) {
		try {
			return medicoService.obtenerUrlFirmaDigital(path);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error al obtener firma digital", "httpStatus", 500));
		}
	}

	@GetMapping("/especialidad_por_id_medico/{idMedico}")
	public ResponseEntity<Map<String, Object>> obtenerEspecialidadPorIdMedico(@PathVariable int idMedico) {
		try {
			return medicoService.obtenerEspecialidadPorIdMedico(idMedico);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error al obtener especialidad", "httpStatus", 500));
		}
	}

	@GetMapping("/medicos-por-especialidad/{idEspecialidad}")
	public ResponseEntity<Map<String, Object>> listarMedicosPorEspecialidad(@PathVariable int idEspecialidad) {
		try {
			return medicoService.listarMedicosPorEspecialidad(idEspecialidad);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error al listar médicos por especialidad", "httpStatus", 500));
		}
	}

	@GetMapping("/dias-disponibles/{idMedico}")
	public ResponseEntity<Map<String, Object>> listarDiasDisponibles(@PathVariable int idMedico) {
		try {
			return medicoService.listarDiasDisponiblesPorMedico(idMedico);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error al listar días disponibles", "httpStatus", 500));
		}
	}

	@GetMapping("/horas-disponibles")
	public ResponseEntity<Map<String, Object>> listarHorasDisponibles(@RequestParam int idMedico,
			@RequestParam String fecha) {
		try {
			return medicoService.listarHorasDisponibles(idMedico, LocalDate.parse(fecha));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error al listar horas disponibles", "httpStatus", 500));
		}
	}

	@GetMapping("/horario-trabajo-medico/{idMedico}")
	public ResponseEntity<Map<String, Object>> listarHorariosDeTrabajoMedico(@PathVariable Integer idMedico) {
		try {
			return medicoService.listarHorariosDeTrabajoMedico(idMedico);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error al listar horarios de trabajo", "httpStatus", 500));
		}
	}
}
