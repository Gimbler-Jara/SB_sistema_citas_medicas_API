package com.cibertec.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class RegistroMedicoDTO {
	private Integer documentTypeId;
    private String dni;
    private String lastName;
    private String middleName;
    private String firstName;
    private LocalDate birthDate;
    private String gender;
    private String telefono;
    private String email;
    private String password;
    private Integer especialidadId;
}
