package com.cibertec.dto;

import java.time.LocalDate;

public class UsuarioRequestDTO {
	public Integer document_type;
    public String dni;
    public String lastName;
    public String middleName;
    public String firstName;
    public LocalDate birthDate;
    public String gender;
    public String telefono;
    public String email;
    public String passwordHash;
    public Integer rol_id;
}
