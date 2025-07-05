package com.proyecto.Edutech_v1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Edutech_v1.model.Curso;
import com.proyecto.Edutech_v1.model.Estudiante;
import com.proyecto.Edutech_v1.model.Gerente;
import com.proyecto.Edutech_v1.model.Instructor;
import com.proyecto.Edutech_v1.repository.CursoRepository;
import com.proyecto.Edutech_v1.repository.EstudianteRepository;
import com.proyecto.Edutech_v1.repository.GerenteRepository;
import com.proyecto.Edutech_v1.repository.InstructorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional

public class CursoService {
    // El servicio maneja la lógica de negocio relacionada con los cursos

    @Autowired
    CursoRepository cursoRepository; // Inyección de dependencia del repositorio de cursos
    @Autowired
    private EstudianteRepository estudianteRepository; // Inyección de dependencia del repositorio de estudiantes
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private GerenteRepository gerenteRepository;

    public boolean asignarGerentePorId(Curso curso, Long gerenteId) {
        Optional<Gerente> gerenteOpt = gerenteRepository.findById(gerenteId);
        if (gerenteOpt.isEmpty()) {
            return false;
        }
        Gerente gerente = gerenteOpt.get();

        // Verifica si ya está asignado
        if (curso.getGerente() != null && curso.getGerente().getIdGerente().equals(gerenteId)) {
            return false;
        }

        curso.setGerente(gerente);
        cursoRepository.save(curso);
        return true;
    }

    public List<Curso> obtenerCursos() {
        return new ArrayList<> (cursoRepository.findAll());
        // Llama al método findAll del repositorio para obtener todos los cursos
        // metodo para obtener todos los cursos
        // Devuelve una lista de cursos como un ArrayList
    }

    public boolean asignarInstructorPorId(Curso curso, Long instructorId) {
        Optional<Instructor> instructorOpt = instructorRepository.findById(instructorId);
        if (instructorOpt.isEmpty()) {
            return false;
        }
        Instructor instructor = instructorOpt.get();

        // Verifica si ya está asignado
        if (curso.getInstructor() != null && curso.getInstructor().getIdIntructor().equals(instructorId)) {
            return false;
        }

        curso.setInstructor(instructor);
        cursoRepository.save(curso);
        return true;
    }

    public boolean inscribirEstudiantePorId(Curso curso, Long estudianteId) {
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(estudianteId);
        if (estudianteOpt.isEmpty()) {
            return false;
        }
        Estudiante estudiante = estudianteOpt.get();

        // Verifica si ya está inscrito
        boolean yaInscrito = curso.getEstudiantesInscritos().stream()
                .anyMatch(e -> e.getIdEstudiante().equals(estudianteId));
        if (yaInscrito) {
            return false;
        }

        estudiante.setCurso(curso);
        curso.getEstudiantesInscritos().add(estudiante);

        estudianteRepository.save(estudiante);
        // Si necesitas, también guarda el curso: cursoRepository.save(curso);

        return true;
    }

    public Optional<Curso> buscarPorId(String id) {
        return cursoRepository.findById(id);
        // metodo para buscar un curso por su ID
        // Llama al método findById del repositorio para buscar un curso por su ID
    }

    public Curso guardarCurso(Curso curso) {
        return cursoRepository.save(curso);
        // metodo para guardar un curso
        // Llama al método save del repositorio para guardar un curso
    }

}
