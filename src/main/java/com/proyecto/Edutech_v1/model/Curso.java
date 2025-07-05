package com.proyecto.Edutech_v1.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Anotación de JPA para indicar que esta clase es una entidad que se mapeará a una tabla en la base de datos
@Table(name = "curso") // Anotación de JPA para especificar el nombre de la tabla en la base de datos
@Data // Anotación de Lombok para generar automáticamente los métodos getter, setter, toString, equals y hashCode
@NoArgsConstructor // Anotación de Lombok para generar un constructor sin parámetros
@AllArgsConstructor // Anotación de Lombok para generar un constructor con todos los parámetros

public class Curso {

    // Atributos de la clase Curso
    @Id
    @Column(name = "codigo_curso", nullable = false, unique = true)
    private String codigoCurso;

    @Column(name = "titulo_curso", nullable = false)
    private String tituloCurso;

    @Column(name = "descripcion_curso", nullable = true)
    private String descripcionCurso;

    @Column(name = "categoria_curso", nullable = false)
    private String categoriaCurso;

    @Column(name = "precio_curso", nullable = false)
    private double precioCurso;

    @Column(name = "duracion_horas_curso", nullable = false)
    private int duracionHorasCurso;

    @Column(name = "certificado_disponible", nullable = false)
    private boolean certificacionDisponibleCurso;

    @Column(name = "resena_curso", nullable = true)
    private String resenaCurso;

    @OneToMany(mappedBy = "curso")
    @JsonManagedReference("curso-estudiante")
    private List<Estudiante> estudiantesInscritos = new java.util.ArrayList<>();
    // Relación uno a muchos con la entidad Estudiante

    @ManyToOne
    @JoinColumn(name = "id_instructor")
    @JsonBackReference("instructor-curso")
    private Instructor instructor;
    // Relación muchos a uno con la entidad Instructor

    @ManyToOne
    @JoinColumn(name = "id_gerente")
    @JsonBackReference("gerente-curso")
    private Gerente gerente;
    // Relación muchos a uno con la entidad Gerente
}
