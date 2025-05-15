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
import com.cibertec.dto.HistorialCitaDTO;
import com.cibertec.dto.MedicamentoInputDTO;
import com.cibertec.repository.CitaMedicaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

@Service
public class CitaMedicaService {

	@Autowired
	private CitaMedicaRepository procedimientosRepository;
	
	@Autowired
    private EntityManager entityManager;

	// 1. Listar citas agendadaspara mostrar al medico
	public ResponseEntity<List<CitasAgendadasResponseDTO>> listarCitasAgendadas(int idMedico) {
		List<CitasAgendadasResponseDTO> citas = procedimientosRepository.listarCitasAgendadas(idMedico);
				//.stream().filter(cita -> !cita.getEstado().equalsIgnoreCase("Atendido")).toList();

		if (citas.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(citas);
		}
	}

	// 2. Registrar disponibilidad de dias y horarios
	public ResponseEntity<Map<String, Object>> registrarDisponibilidadDeCita(int idMedico, int idDiaSemana, int idHora,
			int idEspecialidad) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<Object[]> result = procedimientosRepository.registrarDisponibilidadDeCita(idMedico, idDiaSemana,
					idHora, idEspecialidad);
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
	public void atenderCitaConRecetaCompleta(Integer idCita, String descripcionDiagnostico, List<MedicamentoInputDTO> medicamentos) {
        // Validaciones básicas
        if (descripcionDiagnostico == null || descripcionDiagnostico.isBlank()) {
            throw new IllegalArgumentException("El diagnóstico no puede estar vacío.");
        }

        if (medicamentos == null || medicamentos.isEmpty()) {
            throw new IllegalArgumentException("Debe proporcionar al menos un medicamento.");
        }

        Integer recetaId = atenderCitaYCrearReceta(idCita, descripcionDiagnostico);

        for (MedicamentoInputDTO m : medicamentos) {
            if (m.getMedicamento() != null && !m.getMedicamento().isBlank()) {
                agregarMedicamentoAReceta(recetaId, m.getMedicamento(), m.getIndicaciones());
            }
        }
    }

    private Integer atenderCitaYCrearReceta(Integer idCita, String descripcionDiagnostico) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("sp_atender_cita_y_crear_receta")
                .registerStoredProcedureParameter("idCita", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("descripcionDiagnostico", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("recetaId", Integer.class, ParameterMode.OUT)
                .setParameter("idCita", idCita)
                .setParameter("descripcionDiagnostico", descripcionDiagnostico);

        query.execute();
        return (Integer) query.getOutputParameterValue("recetaId");
    }

    private void agregarMedicamentoAReceta(Integer recetaId, String medicamento, String indicaciones) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("sp_agregar_medicamento_a_receta")
                .registerStoredProcedureParameter("recetaId", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("medicamento", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("indicaciones", String.class, ParameterMode.IN)
                .setParameter("recetaId", recetaId)
                .setParameter("medicamento", medicamento)
                .setParameter("indicaciones", indicaciones);

        query.execute();
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
	
	
	public HistorialCitaDTO obtenerHistorialPorCita(int idCita) {
	    HistorialCitaDTO resultado = procedimientosRepository.obtenerHistorialPorCita(idCita);

	    if (resultado == null) {
	        throw new EntityNotFoundException("No se encontró historial para la cita con ID: " + idCita);
	    }

	    return resultado;
	}
	
	public List<HistorialCitaDTO> obtenerHistorialPorPaciente(int idPaciente) {
		List<HistorialCitaDTO> resultado = procedimientosRepository.obtenerHistorialPorPaciente(idPaciente);

	    if (resultado == null) {
	        throw new EntityNotFoundException("No se encontró historial para la cita con ID: " + idPaciente);
	    }

	    return resultado;
	}

}