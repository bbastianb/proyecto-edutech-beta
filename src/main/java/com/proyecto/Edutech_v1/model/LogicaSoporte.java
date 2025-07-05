package com.proyecto.Edutech_v1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "logica_soporte")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogicaSoporte  {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "turno", nullable = false)
    private String turno;

    @Column(name = "incidentes_resueltos", nullable = false)
    private Integer incidentesResueltos;
    
    @Column(name = "herramientas_soporte", nullable = false)
    private String herramientasSoporte;


}