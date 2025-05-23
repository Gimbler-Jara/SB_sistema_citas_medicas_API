package com.cibertec.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.cibertec.dto.*;

public interface CitaMedicaService {

    ResponseEntity<Map<String, Object>> listarCitasAgendadas(int idMedico);

    ResponseEntity<Map<String, Object>> registrarDisponibilidadDeCita(int idMedico, int idDiaSemana, int idHora, int idEspecialidad);

    ResponseEntity<Map<String, Object>> cambiarEstadoDisponibilidad(int idMedico, int idDiaSemana, int idHora, boolean activo);

    ResponseEntity<Map<String, Object>> agendarCita(int idMedico, int idPaciente, LocalDate fecha, int idHora, int tipoCita, String nombreSala);

    ResponseEntity<Map<String, Object>> eliminarCitaReservado(int idCita);

    ResponseEntity<Map<String, Object>> listarCitasResgistradasPorPaciente(int idPaciente);

    ResponseEntity<Map<String, Object>> obtenerHistorialPorCita(int idCita);

    ResponseEntity<Map<String, Object>> obtenerHistorialPorPaciente(int idPaciente);

    ResponseEntity<Map<String, Object>> atenderCitaConRecetaCompleta(Integer idCita, String diagnostico, List<MedicamentoInputDTO> medicamentos);
}
