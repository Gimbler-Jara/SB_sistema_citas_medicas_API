package com.cibertec.dto;

import lombok.Data;

@Data
public class RegistrarDisponibilidadDeCitaDTO {
	private int idMedico;
    private int idDiaSemana;
    private int idHora;
    private int idEspecialidad;
}
