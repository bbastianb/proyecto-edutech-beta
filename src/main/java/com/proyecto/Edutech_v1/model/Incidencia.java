package com.proyecto.Edutech_v1.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "incidencia")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Solicitudes", nullable = false)
    private String titulo;
    @Column(name = "Descripcion", nullable = false, length = 300)
    private String descripcion;
    @Column(name = "Prioridad")
    private String prioridad;
    @Column(name = "Estado")
    private String estado;
    @Column(name = "Fecha_Reporte")
    private Date fechaReporte;
    @Column(name = "Fecha_Resolucion")
    private Date fechaResolucion;

    @ManyToOne
    private LogicaSoporte asignadoA;
    //Relaciona la incidencia con un instructor que la reporta

    @ManyToOne
    private Instructor reportadoPorInstructor;
    //Relaciona la incidencia con un gerente que la reporta
}