package com.cibertec.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cibertec.dto.CitasAgendadasResponseDTO;
import com.cibertec.dto.CitasReservadasPorPacienteResponseDTO;
import com.cibertec.dto.DiasDisponiblesPorMedicoDTO;
import com.cibertec.dto.HorasDispiniblesDeCitasDTO;
import com.cibertec.dto.MedicosPorEspecialidadDTO;
import com.cibertec.model.Especialidad;
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
    public ResponseEntity<Void> registrarDisponibilidad(int idMedico, int idDiaSemana, int idHora, int idEspecialidad) {
        try {
            procedimientosRepository.registrarDisponibilidad(idMedico, idDiaSemana, idHora, idEspecialidad);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            System.out.println("Error al registrar disponibilidad: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 3. Cambiar estado de disponibilidad
    public ResponseEntity<Void> cambiarEstadoDisponibilidad(int idMedico, int idDiaSemana, int idHora, boolean activo) {
        try {
            procedimientosRepository.cambiarEstadoDisponibilidad(idMedico, idDiaSemana, idHora, activo);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            System.out.println("Error al cambiar estado de disponibilidad: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
        List<HorasDispiniblesDeCitasDTO> horas = procedimientosRepository.listarHorasDisponibles(idMedico, java.sql.Date.valueOf(fecha));
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
    public ResponseEntity<List<CitasReservadasPorPacienteResponseDTO>> listarCitasResgistradasPorPaciente(int idPaciente) {
        List<CitasReservadasPorPacienteResponseDTO> citas = procedimientosRepository.listarCitasProgramadasPorPaciente(idPaciente);
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
}
