package com.cibertec.dto;

import java.util.List;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticoRequestDTO {
    @NotBlank
    private String diagnostico;

    @NotEmpty
    private List<MedicamentoInputDTO> medicamentos;
}

