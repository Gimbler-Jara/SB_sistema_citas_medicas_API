package com.cibertec.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_cita_medica",
       uniqueConstraints = @UniqueConstraint(columnNames = {"idMedico", "fecha", "idHora"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idMedico", nullable = false)
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "dniPaciente", nullable = false)
    private Paciente paciente;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "idHora", nullable = false)
    private Hora hora;

    @ManyToOne
    @JoinColumn(name = "estado")
    private EstadoCita estado;
}

