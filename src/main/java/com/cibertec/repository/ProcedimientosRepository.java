package com.cibertec.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cibertec.dto.CitasAgendadasResponseDTO;
import com.cibertec.dto.CitasReservadasPorPacienteResponseDTO;
import com.cibertec.dto.DiasDisponiblesPorMedicoDTO;
import com.cibertec.dto.DisponibilidadCitaPorMedicoDTO;
import com.cibertec.dto.HorasDispiniblesDeCitasDTO;
import com.cibertec.dto.MedicosPorEspecialidadDTO;
import com.cibertec.model.CitaMedica;
import com.cibertec.model.Disponibilidad;
import com.cibertec.model.Especialidad;

import jakarta.transaction.Transactional;

@Repository
public interface ProcedimientosRepository extends JpaRepository<CitaMedica, Integer> {

	// 1. Listar citas agendadas por médico
	@Query(value = "CALL usp_listar_citas_Agendadas(:idMedico)", nativeQuery = true)
	List<CitasAgendadasResponseDTO> listarCitasAgendadas(@Param("idMedico") int idMedico);

	@Transactional
	@Query(value = "CALL sp_registrar_disponibilidad(:idMedico, :idDiaSemana, :idHora, :idEspecialidad)", nativeQuery = true)
	List<Object[]> registrarDisponibilidad(@Param("idMedico") int idMedico, @Param("idDiaSemana") int idDiaSemana,
			@Param("idHora") int idHora, @Param("idEspecialidad") int idEspecialidad);
	

	// 3. Cambiar estado de disponibilidad
	@Modifying
	@Transactional
	@Query(value = "CALL sp_cambiar_estado_disponibilidad(:idMedico, :idDiaSemana, :idHora, :activo)", nativeQuery = true)
	void cambiarEstadoDisponibilidad(@Param("idMedico") int idMedico, @Param("idDiaSemana") int idDiaSemana,
	    @Param("idHora") int idHora, @Param("activo") int activo);

	

	// 4. Agendar cita
	@Modifying
	@Transactional
	@Query(value = "CALL sp_agendar_cita(:idMedico, :idPaciente, :fecha, :idHora)", nativeQuery = true)
	void agendarCita(@Param("idMedico") int idMedico, @Param("idPaciente") int idPaciente, @Param("fecha") Date fecha,
			@Param("idHora") int idHora);

	// 5. Listar especialidades
	/*
	 * @Query(value = "CALL sp_listar_especialidades()", nativeQuery = true)
	 * List<Object[]> listarEspecialidades();
	 */

	// 6. Listar médicos por especialidad
	@Query(value = "CALL sp_listar_medicos_por_especialidad(:idEspecialidad)", nativeQuery = true)
	List<MedicosPorEspecialidadDTO> listarMedicosPorEspecialidad(@Param("idEspecialidad") int idEspecialidad);

	// 7. Listar días disponibles por médico
	@Query(value = "CALL sp_listar_dias_disponibles_por_medico(:idMedico)", nativeQuery = true)
	List<DiasDisponiblesPorMedicoDTO> listarDiasDisponiblesPorMedico(@Param("idMedico") int idMedico);

	// 8. Listar horas disponibles (OJO: pasa LocalDate como java.sql.Date)
	@Query(value = "CALL sp_listar_horas_disponibles(:idMedico, :fecha)", nativeQuery = true)
	List<HorasDispiniblesDeCitasDTO> listarHorasDisponibles(@Param("idMedico") int idMedico,
			@Param("fecha") Date fecha);

	// 9. Cambiar estado cita a Reservado
	@Modifying
	@Transactional
	@Query(value = "CALL sp_cambiar_estado_cita_Reservado(:idCita)", nativeQuery = true)
	void cambiarEstadoCitaReservado(@Param("idCita") int idCita);

	// 10. Eliminar cita reservada
	@Modifying
	@Transactional
	@Query(value = "CALL sp_eliminar_cita_Reservado(:idCita)", nativeQuery = true)
	void eliminarCitaReservado(@Param("idCita") int idCita);

	// 11. Listar citas porgramadas por pacientes
	@Query(value = "CALL sp_listar_citas_programadas_por_paciente(:idPaciente)", nativeQuery = true)
	List<CitasReservadasPorPacienteResponseDTO> listarCitasProgramadasPorPaciente(@Param("idPaciente") int idPaciente);

	// 12. Obtener Especialidad por id medico
	@Query(value = "CALL sp_consultar_especialidad_por_id_medico(:idMedico)", nativeQuery = true)
	Especialidad obtenerEspecialidadPorIdMedico(@Param("idMedico") int idMedico);
	
	//13. Listar disponiblidade de los medicos
	@Query(value = "CALL sp_listar_disponibilidades_por_medico(:idMedico)", nativeQuery = true)
    List<DisponibilidadCitaPorMedicoDTO> listarDisponibilidadesPorMedico(@Param("idMedico") Integer idMedico);

}
