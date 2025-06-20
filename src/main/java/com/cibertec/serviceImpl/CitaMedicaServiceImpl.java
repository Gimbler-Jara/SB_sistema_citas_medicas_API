package com.cibertec.serviceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cibertec.dto.*;
import com.cibertec.repository.CitaMedicaRepository;
import com.cibertec.service.CitaMedicaService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

@Service
public class CitaMedicaServiceImpl implements CitaMedicaService {

    @Autowired
    private CitaMedicaRepository procedimientosRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public ResponseEntity<Map<String, Object>> listarCitasAgendadas(int idMedico) {
        Map<String, Object> response = new HashMap<>();
        List<CitasAgendadasResponseDTO> citas = procedimientosRepository.listarCitasAgendadas(idMedico);
        if (citas.isEmpty()) {
            response.put("mensaje", "No hay citas agendadas");
            response.put("httpStatus", HttpStatus.NO_CONTENT.value());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("mensaje", "Citas agendadas encontradas");
        response.put("httpStatus", HttpStatus.OK.value());
        response.put("citas", citas);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Map<String, Object>> registrarDisponibilidadDeCita(int idMedico, int idDiaSemana, int idHora, int idEspecialidad) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Object[]> result = procedimientosRepository.registrarDisponibilidadDeCita(idMedico, idDiaSemana, idHora, idEspecialidad);
            if (!result.isEmpty()) {
                Object[] row = result.get(0);
                response.put("mensaje", row[1].toString());
                response.put("httpStatus", HttpStatus.OK.value());
            } else {
                response.put("mensaje", "No se pudo obtener respuesta del procedimiento");
                response.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } catch (Exception e) {
            response.put("mensaje", "Error al registrar disponibilidad");
            response.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());
        }
        return ResponseEntity.status((Integer) response.get("httpStatus")).body(response);
    }

    @Override
    public ResponseEntity<Map<String, Object>> cambiarEstadoDisponibilidad(int idMedico, int idDiaSemana, int idHora, boolean activo) {
        Map<String, Object> response = new HashMap<>();
        try {
            procedimientosRepository.cambiarEstadoDisponibilidad(idMedico, idDiaSemana, idHora, activo ? 1 : 0);
            response.put("mensaje", "Estado de disponibilidad actualizado");
            response.put("httpStatus", HttpStatus.OK.value());
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al cambiar estado de disponibilidad");
            response.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> agendarCita(int idMedico, int idPaciente, LocalDate fecha, int idHora, int tipoCita, String nombreSala) {
        Map<String, Object> response = new HashMap<>();
        try {
            procedimientosRepository.agendarCita(idMedico, idPaciente, java.sql.Date.valueOf(fecha), idHora, tipoCita, nombreSala);
            response.put("mensaje", "Cita agendada correctamente");
            response.put("httpStatus", HttpStatus.CREATED.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al agendar cita");
            response.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> atenderCitaConRecetaCompleta(Integer idCita, String diagnostico, List<MedicamentoInputDTO> medicamentos) {
        Map<String, Object> response = new HashMap<>();
        if (diagnostico == null || diagnostico.isBlank()) {
            response.put("mensaje", "El diagnóstico no puede estar vacío.");
            response.put("httpStatus", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(response);
        }
        if (medicamentos == null || medicamentos.isEmpty()) {
            response.put("mensaje", "Debe proporcionar al menos un medicamento.");
            response.put("httpStatus", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(response);
        }
        try {
            Integer recetaId = atenderCitaYCrearReceta(idCita, diagnostico);
            for (MedicamentoInputDTO m : medicamentos) {
                if (m.getMedicamento() != null && !m.getMedicamento().isBlank()) {
                    agregarMedicamentoAReceta(recetaId, m.getMedicamento(), m.getIndicaciones());
                }
            }
            response.put("mensaje", "Cita atendida y receta registrada correctamente");
            response.put("httpStatus", HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al atender cita con receta");
            response.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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

    @Override
    public ResponseEntity<Map<String, Object>> eliminarCitaReservado(int idCita) {
        Map<String, Object> response = new HashMap<>();
        try {
            procedimientosRepository.eliminarCitaReservado(idCita);
            response.put("mensaje", "Cita eliminada correctamente");
            response.put("httpStatus", HttpStatus.NO_CONTENT.value());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al eliminar cita");
            response.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> listarCitasResgistradasPorPaciente(int idPaciente) {
        Map<String, Object> response = new HashMap<>();
        List<CitasReservadasPorPacienteResponseDTO> citas = procedimientosRepository.listarCitasProgramadasPorPaciente(idPaciente);
        if (citas.isEmpty()) {
            response.put("mensaje", "No hay citas registradas para este paciente");
            response.put("httpStatus", HttpStatus.NO_CONTENT.value());
            response.put("citas", citas);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("mensaje", "Citas reservadas encontradas");
        response.put("httpStatus", HttpStatus.OK.value());
        response.put("citas", citas);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Map<String, Object>> obtenerHistorialPorCita(int idCita) {
        Map<String, Object> response = new HashMap<>();
        HistorialCitaDTO resultado = procedimientosRepository.obtenerHistorialPorCita(idCita);
        if (resultado == null) {
            response.put("mensaje", "No se encontró historial para la cita con ID: " + idCita);
            response.put("httpStatus", HttpStatus.NOT_FOUND.value());
            response.put("datos", resultado);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
                
        response.put("mensaje", "Historial encontrado");
        response.put("httpStatus", HttpStatus.OK.value());
        response.put("datos", resultado);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Map<String, Object>> obtenerHistorialPorPaciente(int idPaciente) {
        Map<String, Object> response = new HashMap<>();
        List<HistorialCitaDTO> resultado = procedimientosRepository.obtenerHistorialPorPaciente(idPaciente);
        if (resultado == null || resultado.isEmpty()) {
            response.put("mensaje", "No se encontró historial para el paciente con ID: " + idPaciente);
            response.put("httpStatus", HttpStatus.NO_CONTENT.value());
            response.put("datos",  new ArrayList<>());
            return ResponseEntity.ok(response);
        }
        
        response.put("mensaje", "Historial del paciente encontrado");
        response.put("httpStatus", HttpStatus.OK.value());
        response.put("datos", resultado);
        return ResponseEntity.ok(response);
    }
}
