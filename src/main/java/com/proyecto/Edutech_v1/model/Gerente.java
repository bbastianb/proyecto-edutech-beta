package com.proyecto.Edutech_v1.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gerente") // Anotación de JPA para especificar el nombre de la tabla en la base de datos
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Gerente { //extiende Usuario
    // Clase que representa a un gerente en el sistema, extendiendo la clase Usuario

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gerente")
    private Long idGerente;

    @Column(name = "nombre_gerente", nullable = false, length = 100)
    private String nombreGere;

    @Column(name = "apellidos_gerente", nullable = false, length = 100)
    private String apellidoGere;

    @Column(name = "email_gerente", nullable = false, unique = true)
    private String emailGere;

    @Column(name = "contraseña_gerente", nullable = false, length = 100)
    private String contraseñaGere;

    @Column(name = "telefono_gerente", length = 20)
    private String telefonoGere;

    @Column(name = "fecha_registro_gere", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;

    @Column(name = "area_especialidad", nullable = false)
    private String especialidad;

    @Column(name = "cursos_gestionados")
    private Double cursosGestionados;

    @OneToMany(mappedBy = "gerente")
    @JsonManagedReference("gerente-curso")
    private List<Curso> cursos = new ArrayList<>();
}
