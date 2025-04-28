package com.cibertec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibertec.model.Disponibilidad;
import com.cibertec.model.Medico;

@Repository
public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Integer>{
	List<Disponibilidad> findByMedico_IdUsuario(Integer idUsuario);
}
