package com.cibertec.dto;

import lombok.Data;

@Data
public class CambiarEstadoDisponibilidadDeCitaDTO {
	private int idMedico;
	private int idDiaSemana;
	private int idHora;
	private boolean activo;
}
