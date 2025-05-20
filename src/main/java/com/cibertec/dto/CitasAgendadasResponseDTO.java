package com.cibertec.dto;

import java.sql.Date;

public interface CitasAgendadasResponseDTO {
	Integer getId();
	Date getFecha();
	String getHora();
	String getEstado();
	Integer getPacienteId();
	String getPacienteNombre();
	Integer getMedicoId();
	String getMedicoNombre();
	String getEspecialidad();
	Integer getTipo_cita();
	String getNombre_sala();
}
