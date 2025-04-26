package com.cibertec.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cibertec.dto.*;
import com.cibertec.service.ProcedimientosService;

@RestController
@RequestMapping("/api/procedimientos")
public class ProcedimientosController {

	@Autowired
	private ProcedimientosService service;

	@GetMapping("/citas-agendadas/{idMedico}")
	public ResponseEntity<?> listarCitasAgendadas(@PathVariable("idMedico") int idMedico) {
		return service.listarCitasAgendadas(idMedico);
	}

	@PostMapping("/registrar-disponibilidad")
	public ResponseEntity<?> registrarDisponibilidad(@RequestBody RegistrarDisponibilidadDeCitaDTO req) {
		return service.registrarDisponibilidad(req.getIdMedico(), req.getIdDiaSemana(), req.getIdHora(),
				req.getIdEspecialidad());
	}

	@PutMapping("/cambiar-estado-disponibilidad")
	public ResponseEntity<?> cambiarEstadoDisponibilidad(@RequestBody CambiarEstadoDisponibilidadDeCitaDTO req) {
		return service.cambiarEstadoDisponibilidad(req.getIdMedico(), req.getIdDiaSemana(), req.getIdHora(),
				req.isActivo());
	}

	@PostMapping("/agendar-cita")
	public ResponseEntity<?> agendarCita(@RequestBody AgendarCitaRequestDTO req) {
		return service.agendarCita(req.getIdMedico(), req.getIdPaciente(), req.getFecha(), req.getIdHora());
	}

	@GetMapping("/medicos-por-especialidad/{idEspecialidad}")
	public ResponseEntity<?> listarMedicosPorEspecialidad(@PathVariable("idEspecialidad") int idEspecialidad) {
		return service.listarMedicosPorEspecialidad(idEspecialidad);
	}

	@GetMapping("/dias-disponibles/{idMedico}")
	public ResponseEntity<?> listarDiasDisponibles(@PathVariable("idMedico") int idMedico) {
		return service.listarDiasDisponiblesPorMedico(idMedico);
	}

	@GetMapping("/horas-disponibles")
	public ResponseEntity<?> listarHorasDisponibles(@RequestParam int idMedico, @RequestParam String fecha) {
		return service.listarHorasDisponibles(idMedico, LocalDate.parse(fecha));
	}

	@PutMapping("/cambiar-estado-cita")
	public ResponseEntity<?> cambiarEstadoCita(@RequestParam int idCita) {
		return service.cambiarEstadoCitaReservado(idCita);
	}

	@DeleteMapping("/eliminar-cita/{idCita}")
	public ResponseEntity<?> eliminarCita(@PathVariable("idCita") int idCita) {
		return service.eliminarCitaReservado(idCita);
	}

	@GetMapping("/citas-reservadas-paciente/{idPaciente}")
	public ResponseEntity<?> listarCitasReservadasPorPaciente(@PathVariable("idPaciente") int idPaciente) {
		return service.listarCitasResgistradasPorPaciente(idPaciente);
	}

	@GetMapping("/especialidad_por_id_medico/{idMedico}")
	public ResponseEntity<?> obtenerEspecialidadPorIdMedico(@PathVariable("idMedico") int idMedico) {
		return service.obtenerEspecialidadPorIdMedico(idMedico);
	}

}
