package com.cibertec.dto;

import com.cibertec.enums.LoginResultado;
import com.cibertec.model.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
	public Usuario usuario;
	public LoginResultado resultado;
}
