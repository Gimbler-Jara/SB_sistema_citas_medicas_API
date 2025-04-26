package com.cibertec.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CitasReservadasPorPacienteResponseDTO {
	public int idMedico;
    public String medico;
    public String especialidad;
    public String estado;
    public Date fecha;
    public int idHora;
}
