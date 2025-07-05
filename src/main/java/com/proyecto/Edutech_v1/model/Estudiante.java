package com.proyecto.Edutech_v1.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Anotación de JPA para indicar que esta clase es una entidad que se mapeará a una tabla en la base de datos
@Table(name = "estudiante") // Anotación de JPA para especificar el nombre de la tabla en la base de datos
@Data // Anotación de Lombok para generar automáticamente los métodos getter, setter, toString, equals y hashCode
@NoArgsConstructor // Anotación de Lombok para generar un constructor sin parámetros
@AllArgsConstructor// Anotación de Lombok para generar un constructor con todos los parámetros

public class Estudiante {

    // Atributos de la clase Estudiante
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// Generación automática del ID de estudiante
    private Long idEstudiante;

    @Column(name = "nombre_estudiante", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellidos_estudiante", nullable = false, length = 100)
    private String apellido;

    @Column(name = "email_estudiante", nullable = false, unique = true)
    private String email;

    @Column(name = "contraseña_estudiante", nullable = false, length = 100)
    private String contraseña;

    @Column(name = "telefono_estudiante", length = 20)
    private String telefono;

    @Column(name = "fecha_registro_estu", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;

    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;

    @Column(name = "cursos_inscrito", nullable = false)
    private int cursoIncrito;

    @Column(name = "saldo_disponible", nullable = true)
    private double saldoDisponible;

    @ManyToOne
    @JoinColumn(name = "codigo_curso") // Relación muchos a uno con la entidad Curso
    @JsonBackReference("curso-estudiante") // Evita la serialización recursiva
    private Curso curso;

}
