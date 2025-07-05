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
@Table(name = "instructor") // Anotación de JPA para especificar el nombre de la tabla en la base de datos
@Data // Anotación de Lombok para generar automáticamente los métodos getter, setter, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_instructor")
    private Long idIntructor;

    @Column(name="nombre_instructor",nullable = false, length = 100)
    private String nombreIn;

    @Column(name="apellido_instructor",nullable = false, length = 100)
    private String apellidoIn;

    @Column(name="email_instructor",nullable = false, unique = true)
    private String emailIn;

    @Column(name="contraseña_instructor", nullable = false, length = 100)
    private String contraseñaIn;

    @Column(name="telefono_instructor",length = 20)
    private String telefonoIn;

    @Column(name = "fecha_registro_ins", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;

    @Column(name = "especialidad", nullable = false)
    private String especialidad;

    @Column(name = "cursos_impartidos")
    private Integer cursosImpartidos;

    @Column(name = "nombres_cursos_impartidos")
    private String nomCursosImpartidos;

    @Column(name = "certificado")
    private Boolean certificado;

    @OneToMany(mappedBy = "instructor")
    @JsonManagedReference("instructor-curso")
    private List<Curso> cursos = new ArrayList<>();
    // Relación con la entidad Curso, donde un instructor puede impartir varios cursos
}
