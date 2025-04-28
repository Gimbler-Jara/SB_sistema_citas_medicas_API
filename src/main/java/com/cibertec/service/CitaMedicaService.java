package com.cibertec.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cibertec.dto.CitasAgendadasResponseDTO;
import com.cibertec.dto.CitasReservadasPorPacienteResponseDTO;
import com.cibertec.repository.CitaMedicaRepository;

@Service
public class CitaMedicaService {

	@Autowired
	private CitaMedicaRepository procedimientosRepository;

	// 1. Listar citas agendadas por médico
	public ResponseEntity<List<CitasAgendadasResponseDTO>> listarCitasAgendadas(int idMedico) {
		List<CitasAgendadasResponseDTO> citas = procedimientosRepository.listarCitasAgendadas(idMedico);
		if (citas.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(citas);
		}
	}

	// 2. Registrar disponibilidad
	public ResponseEntity<Map<String, Object>> registrarDisponibilidadDeCita(int idMedico, int idDiaSemana, int idHora,
			int idEspecialidad) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<Object[]> result = procedimientosRepository.registrarDisponibilidadDeCita(idMedico, idDiaSemana, idHora,
					idEspecialidad);
			if (!result.isEmpty()) {
				Object[] row = result.get(0);
				boolean success = Integer.parseInt(row[0].toString()) == 1;
				String message = row[1].toString();
				response.put("success", success);
				response.put("message", message);
			} else {
				response.put("success", false);
				response.put("message", "No se pudo obtener respuesta del procedimiento");
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "Error al registrar disponibilidad");
			response.put("errors", List.of(e.getMessage()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	// 3. Cambiar estado de disponibilidad
	public ResponseEntity<Map<String, Object>> cambiarEstadoDisponibilidad(int idMedico, int idDiaSemana, int idHora,
			boolean activo) {
		Map<String, Object> response = new HashMap<>();
		try {
			int activoInt = activo ? 1 : 0;
			procedimientosRepository.cambiarEstadoDisponibilidad(idMedico, idDiaSemana, idHora, activoInt);
			// Aquí ya no puedes saber si se modificó o no, solo que no falló la consulta
			response.put("success", true);
			response.put("message", "Estado de disponibilidad actualizado (o ya estaba igual)");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "Error al cambiar estado de disponibilidad");
			response.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	// 4. Agendar cita
	public ResponseEntity<Void> agendarCita(int idMedico, int idPaciente, LocalDate fecha, int idHora) {
		try {
			procedimientosRepository.agendarCita(idMedico, idPaciente, java.sql.Date.valueOf(fecha), idHora);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			System.out.println("Error al agendar cita: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	

	

	// 8. Cambiar estado cita a reservado
	public ResponseEntity<Void> cambiarEstadoCitaReservadoAtendio(int idCita) {
		try {
			procedimientosRepository.cambiarEstadoCitaReservadoAtendio(idCita);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			System.out.println("Error al cambiar estado de cita: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// 9. Eliminar cita reservada
	public ResponseEntity<Void> eliminarCitaReservado(int idCita) {
		try {
			procedimientosRepository.eliminarCitaReservado(idCita);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			System.out.println("Error al eliminar cita: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// 10. Listar citas registradas por paciente
	public ResponseEntity<List<CitasReservadasPorPacienteResponseDTO>> listarCitasResgistradasPorPaciente(
			int idPaciente) {
		List<CitasReservadasPorPacienteResponseDTO> citas = procedimientosRepository
				.listarCitasProgramadasPorPaciente(idPaciente);
		if (citas.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(citas);
		}
	}
}