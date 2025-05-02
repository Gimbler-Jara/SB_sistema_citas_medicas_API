package com.cibertec.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MedicoActualizacionDTO {
	private Integer idUsuario;
	private String firstName;
	private String middleName;
	private String lastName;
	private String telefono;
	private LocalDate birthDate;
	private String gender;
	private Integer especialidadId;
}
