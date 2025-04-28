package com.cibertec.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.dto.MedicoActualizacionDTO;
import com.cibertec.dto.RegistroMedicoDTO;
import com.cibertec.model.Medico;
import com.cibertec.service.MedicoService;


@RestController
@RequestMapping("/api/medicos")
public class MedicoController {
	
	@Autowired
	private MedicoService medicoService;
	
	
	@GetMapping
    public ResponseEntity<List<Medico>> listarEspecialidades() {
        return medicoService.listarMedicos();
    }
	
	@PostMapping
	public ResponseEntity<Medico> registrarMedico(@RequestBody RegistroMedicoDTO dto) {
		Medico medico = medicoService.registrarMedico(dto);
		return ResponseEntity.ok(medico);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> actualizarMedico(@PathVariable Integer id, @RequestBody MedicoActualizacionDTO dto) {
	    return medicoService.actualizarMedico(id, dto);
	}

	
	@DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMedico(@PathVariable Integer id) {
        return medicoService.eliminarMedico(id);
    }
	
	@GetMapping("/especialidad_por_id_medico/{idMedico}")
	public ResponseEntity<?> obtenerEspecialidadPorIdMedico(@PathVariable("idMedico") int idMedico) {
		return medicoService.obtenerEspecialidadPorIdMedico(idMedico);
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
	
	@GetMapping("/horario-trabajo-medico/{idMedico}")
    public ResponseEntity<?> listarHorariosDeTrabajoMedico(@PathVariable Integer idMedico) {
        return medicoService.listarHorariosDeTrabajoMedico(idMedico);
    }

}
