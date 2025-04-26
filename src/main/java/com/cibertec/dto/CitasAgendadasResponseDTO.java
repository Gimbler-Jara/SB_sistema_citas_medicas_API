package com.cibertec.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitasAgendadasResponseDTO {
	public int id;
	public Date fecha;
	public String hora;
	public String estado;
	public String pacienteID;
	public String pacienteNombre;
	public int medicoId;
	public String medicoNombre;
	public String especialidad;
}
