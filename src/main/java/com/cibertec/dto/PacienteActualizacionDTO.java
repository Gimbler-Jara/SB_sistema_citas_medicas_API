package com.cibertec.dto;

import lombok.Data;

@Data
public class PacienteActualizacionDTO {
	private Integer idUsuario;
	private String firstName;
	private String middleName;
	private String lastName;
	private String telefono;
	private String birthDate; 
	private String gender;
	private String dni;
	private String email;
    private String password;
	private Integer documentTypeId;

}
