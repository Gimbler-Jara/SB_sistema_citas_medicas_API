package com.cibertec.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_receta_medica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_diagnostico", nullable = false)
    private Diagnostico diagnostico;

    @Column(nullable = false)
    private LocalDate fecha;

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL)
    private List<MedicamentoReceta> medicamentos;
}

