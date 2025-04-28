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
import com.cibertec.dto.DiasDisponiblesPorMedicoDTO;
import com.cibertec.dto.DisponibilidadCitaPorMedicoDTO;
import com.cibertec.dto.HorasDispiniblesDeCitasDTO;
import com.cibertec.dto.MedicosPorEspecialidadDTO;
import com.cibertec.model.Disponibilidad;
import com.cibertec.model.Especialidad;
import com.cibertec.repository.DisponibilidadRepository;
import com.cibertec.repository.ProcedimientosRepository;

@Service
public class ProcedimientosService {

	@Autowired
	private ProcedimientosRepository procedimientosRepository;

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
	public ResponseEntity<Map<String, Object>> registrarDisponibilidad(int idMedico, int idDiaSemana, int idHora,
			int idEspecialidad) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<Object[]> result = procedimientosRepository.registrarDisponibilidad(idMedico, idDiaSemana, idHora,
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

	// 5. Listar médicos por especialidad
	public ResponseEntity<List<MedicosPorEspecialidadDTO>> listarMedicosPorEspecialidad(int idEspecialidad) {
		List<MedicosPorEspecialidadDTO> medicos = procedimientosRepository.listarMedicosPorEspecialidad(idEspecialidad);
		if (medicos.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(medicos);
		}
	}

	// 6. Listar días disponibles por médico
	public ResponseEntity<List<DiasDisponiblesPorMedicoDTO>> listarDiasDisponiblesPorMedico(int idMedico) {
		List<DiasDisponiblesPorMedicoDTO> dias = procedimientosRepository.listarDiasDisponiblesPorMedico(idMedico);
		if (dias.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(dias);
		}
	}

	// 7. Listar horas disponibles
	public ResponseEntity<List<HorasDispiniblesDeCitasDTO>> listarHorasDisponibles(int idMedico, LocalDate fecha) {
		List<HorasDispiniblesDeCitasDTO> horas = procedimientosRepository.listarHorasDisponibles(idMedico,
				java.sql.Date.valueOf(fecha));
		if (horas.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(horas);
		}
	}

	// 8. Cambiar estado cita a reservado
	public ResponseEntity<Void> cambiarEstadoCitaReservado(int idCita) {
		try {
			procedimientosRepository.cambiarEstadoCitaReservado(idCita);
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

	// 11. Obtener especialidad por id de médico
	public ResponseEntity<Especialidad> obtenerEspecialidadPorIdMedico(int idMedico) {
		Especialidad especialidad = procedimientosRepository.obtenerEspecialidadPorIdMedico(idMedico);

		if (especialidad == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(especialidad);
		}
	}

	public ResponseEntity<Map<String, Object>> obtenerDisponibilidadesPorMedico(Integer idMedico) {
		List<DisponibilidadCitaPorMedicoDTO> lista = procedimientosRepository.listarDisponibilidadesPorMedico(idMedico);
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Disponibilidades encontradas");
		response.put("data", lista);
		return ResponseEntity.ok(response);
	}
}
