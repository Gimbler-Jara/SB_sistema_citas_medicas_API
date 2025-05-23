package com.cibertec.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cibertec.dto.*;
import com.cibertec.service.CitaMedicaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cita-medica")
public class CitaMedicaController {

	@Autowired
	private CitaMedicaService service;

	@GetMapping("/citas-agendadas/{idMedico}")
	public ResponseEntity<Map<String, Object>> listarCitasAgendadas(@PathVariable("idMedico") int idMedico) {
		try {
			return service.listarCitasAgendadas(idMedico);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error al listar citas agendadas", "httpStatus", 500));
		}
	}

	@PostMapping("/registrar-disponibilidad")
	public ResponseEntity<Map<String, Object>> registrarDisponibilidadDeCita(@RequestBody RegistrarDisponibilidadDeCitaDTO req) {
		try {
			return service.registrarDisponibilidadDeCita(req.getIdMedico(), req.getIdDiaSemana(), req.getIdHora(), req.getIdEspecialidad());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error al registrar disponibilidad", "httpStatus", 500));
		}
	}

	@PutMapping("/cambiar-estado-disponibilidad")
	public ResponseEntity<Map<String, Object>> cambiarEstadoDisponibilidad(@RequestBody CambiarEstadoDisponibilidadDeCitaDTO req) {
		try {
			return service.cambiarEstadoDisponibilidad(req.getIdMedico(), req.getIdDiaSemana(), req.getIdHora(), req.isActivo());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error al cambiar estado de disponibilidad", "httpStatus", 500));
		}
	}

	@PostMapping("/agendar-cita")
	public ResponseEntity<Map<String, Object>> agendarCita(@RequestBody AgendarCitaRequestDTO req) {
		try {
			return service.agendarCita(req.getIdMedico(), req.getIdPaciente(), req.getFecha(), req.getIdHora(), req.getTipoCita(), req.getNombreSala());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error al agendar cita", "httpStatus", 500));
		}
	}

	@PostMapping("/cambiar-estado-cita-reservado-atendido/{idCita}")
	public ResponseEntity<Map<String, Object>> atenderCita(@PathVariable("idCita") int idCita, @Valid @RequestBody DiagnosticoRequestDTO request) {
		try {
			return service.atenderCitaConRecetaCompleta(idCita, request.getDiagnostico(), request.getMedicamentos());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error al atender la cita", "httpStatus", 500));
		}
	}

	@DeleteMapping("/eliminar-cita/{idCita}")
	public ResponseEntity<Map<String, Object>> eliminarCita(@PathVariable("idCita") int idCita) {
		try {
			return service.eliminarCitaReservado(idCita);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error al eliminar cita", "httpStatus", 500));
		}
	}

	@GetMapping("/citas-reservadas-paciente/{idPaciente}")
	public ResponseEntity<Map<String, Object>> listarCitasReservadasPorPaciente(@PathVariable("idPaciente") int idPaciente) {
		try {
			return service.listarCitasResgistradasPorPaciente(idPaciente);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error al listar citas reservadas", "httpStatus", 500));
		}
	}

	@GetMapping("/historial/{idCita}")
	public ResponseEntity<Map<String, Object>> obtenerHistorialPorCita(@PathVariable("idCita") int idCita) {
		try {
			return service.obtenerHistorialPorCita(idCita);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error al obtener historial de la cita", "httpStatus", 500));
		}
	}

	@GetMapping("/historial-paciente/{idPaciente}")
	public ResponseEntity<Map<String, Object>> obtenerHistorialPorPaciente(@PathVariable("idPaciente") int idPaciente) {
		try {
			return service.obtenerHistorialPorPaciente(idPaciente);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error al obtener historial del paciente", "httpStatus", 500));
		}
	}
}
