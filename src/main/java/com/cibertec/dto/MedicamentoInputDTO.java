package com.cibertec.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoInputDTO {
	
    @NotBlank
    private String medicamento;

    private String indicaciones;
}

