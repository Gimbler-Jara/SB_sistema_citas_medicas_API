package com.cibertec.controller;


import org.springframework.beans.factory.annotation.Autowired;
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
	public ResponseEntity<?> listarCitasAgendadas(@PathVariable("idMedico") int idMedico) {
		return service.listarCitasAgendadas(idMedico);
	}

	@PostMapping("/registrar-disponibilidad")
	public ResponseEntity<?> registrarDisponibilidadDeCita(@RequestBody RegistrarDisponibilidadDeCitaDTO req) {
	    return service.registrarDisponibilidadDeCita(req.getIdMedico(), req.getIdDiaSemana(), req.getIdHora(),
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

	
	@PostMapping("/cambiar-estado-cita-reservado-atendido/{idCita}")
    public ResponseEntity<Void> atenderCita(@PathVariable("idCita") int idCita,@Valid @RequestBody DiagnosticoRequestDTO request) {
        service.atenderCitaConRecetaCompleta(idCita, request.getDiagnostico(), request.getMedicamentos());
        return ResponseEntity.ok().build();
    }

	@DeleteMapping("/eliminar-cita/{idCita}")
	public ResponseEntity<?> eliminarCita(@PathVariable("idCita") int idCita) {
		return service.eliminarCitaReservado(idCita);
	}

	@GetMapping("/citas-reservadas-paciente/{idPaciente}")
	public ResponseEntity<?> listarCitasReservadasPorPaciente(@PathVariable("idPaciente") int idPaciente) {
		return service.listarCitasResgistradasPorPaciente(idPaciente);
	}

	
	@GetMapping("/historial/{idCita}")
	public ResponseEntity<HistorialCitaDTO> obtenerHistorial(@PathVariable("idCita") int idCita) {
	    HistorialCitaDTO dto = service.obtenerHistorialPorCita(idCita);
	    return ResponseEntity.ok(dto);
	}


}
