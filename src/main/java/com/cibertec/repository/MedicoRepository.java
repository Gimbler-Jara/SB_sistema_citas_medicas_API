package com.cibertec.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cibertec.dto.DiasDisponiblesPorMedicoDTO;
import com.cibertec.dto.DisponibilidadCitaPorMedicoDTO;
import com.cibertec.dto.HorasDispiniblesDeCitasDTO;
import com.cibertec.dto.MedicosPorEspecialidadDTO;
import com.cibertec.model.Especialidad;
import com.cibertec.model.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {

	@Query(value = "CALL sp_consultar_especialidad_por_id_medico(:idMedico)", nativeQuery = true)
	Especialidad obtenerEspecialidadPorIdMedico(@Param("idMedico") int idMedico);

	@Query(value = "CALL sp_listar_medicos_por_especialidad(:idEspecialidad)", nativeQuery = true)
	List<MedicosPorEspecialidadDTO> listarMedicosPorEspecialidad(@Param("idEspecialidad") int idEspecialidad);

	@Query(value = "CALL sp_listar_dias_disponibles_por_medico(:idMedico)", nativeQuery = true)
	List<DiasDisponiblesPorMedicoDTO> listarDiasDisponiblesPorMedico(@Param("idMedico") int idMedico);

	@Query(value = "CALL sp_listar_horas_disponibles(:idMedico, :fecha)", nativeQuery = true)
	List<HorasDispiniblesDeCitasDTO> listarHorasDisponibles(@Param("idMedico") int idMedico,
			@Param("fecha") Date fecha);

	@Query(value = "CALL sp_listar_horarios_de_trabajo_medico(:idMedico)", nativeQuery = true)
	List<DisponibilidadCitaPorMedicoDTO> listarHorariosDeTrabajoMedico(@Param("idMedico") Integer idMedico);

}
