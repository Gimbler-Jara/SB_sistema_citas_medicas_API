package com.cibertec.dto;

import java.time.LocalDate;


public interface HistorialCitaDTO {
    Integer getIdCita();
    Integer getIdPaciente();
    String getPaciente();
    Integer getIdMedico();
    String getMedico();
    LocalDate getFecha();
    String getHora();
    String getDiagnostico();
    String getReceta();
}