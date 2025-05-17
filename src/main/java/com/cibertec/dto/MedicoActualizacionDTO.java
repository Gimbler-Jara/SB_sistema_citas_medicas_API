package com.cibertec.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MedicoActualizacionDTO {
	private Integer idUsuario;
	private Integer documentTypeId;
	private String dni;
	private String firstName;
	private String middleName;
	private String lastName;
	private LocalDate birthDate;
	private String gender;
	private String telefono;
	private String email;
	private String password;
	private Integer especialidadId;
}
