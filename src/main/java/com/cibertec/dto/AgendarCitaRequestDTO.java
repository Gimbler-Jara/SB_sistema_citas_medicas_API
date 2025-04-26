package com.cibertec.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class AgendarCitaRequestDTO {
	private int idMedico;
    private int idPaciente;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;
    private int idHora;
}
