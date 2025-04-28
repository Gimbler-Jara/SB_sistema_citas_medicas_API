package com.cibertec.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DisponibilidadCitaPorMedicoDTO {
	private Integer idDisponibilidad;
    private Integer idDia;
    private String dia;
    private Integer idHora;
    private String hora;
    private Boolean activo;
}
