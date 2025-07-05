package com.proyecto.Edutech_v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.proyecto.Edutech_v1.model.Instructor;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar instructores por especialidad o calificación
    // promedio

    List<Instructor> findByEspecialidad(String especialidad);

    @Query("SELECT DISTINCT i FROM Instructor i LEFT JOIN FETCH i.cursos")
    List<Instructor> findAllWithCursos();

}
