package com.proyecto.Edutech_v1.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Edutech_v1.model.Estudiante;
import com.proyecto.Edutech_v1.repository.EstudianteRepository;

@Service // Anotación para indicar que esta clase es un servicio
public class EstudianteService {

    @Autowired
    EstudianteRepository estudianteRepository; // Inyección de dependencia del repositorio de estudiantes

    public ArrayList<Estudiante> obtenerEstudiantes() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        estudianteRepository.findAll().forEach(lista::add);
        return lista;
    }

    public Estudiante guardarEstudiante(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
        // Llama al método save del repositorio para guardar un estudiante
    }

    public Estudiante actualizarEstudiante(Long id, Estudiante datosEstudiante) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        estudiante.setNombre(datosEstudiante.getNombre());
        estudiante.setApellido(datosEstudiante.getApellido());
        estudiante.setEmail(datosEstudiante.getEmail());
        estudiante.setContraseña(datosEstudiante.getContraseña());
        estudiante.setTelefono(datosEstudiante.getTelefono());
        estudiante.setMetodoPago(datosEstudiante.getMetodoPago());
        estudiante.setCursoIncrito(datosEstudiante.getCursoIncrito());
        estudiante.setSaldoDisponible(datosEstudiante.getSaldoDisponible());
        // Actualiza otros campos si es necesario
        // No se puede actualizar el ID, ya que es la clave primaria y la fecha de
        // registro no debe cambiar
        return estudianteRepository.save(estudiante);
    }

    public void eliminarEstudiante(Long id) {
        estudianteRepository.deleteById(id);
    }

    public List<Estudiante> obtenerEstudiantesConInstructorYCursos() {
        List<Estudiante> estudiantes = new ArrayList<>();
        estudianteRepository.findAll().forEach(estudiantes::add);
        return estudiantes;
    }
}
