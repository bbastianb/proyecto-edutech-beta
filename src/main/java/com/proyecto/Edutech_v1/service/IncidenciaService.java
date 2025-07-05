package com.proyecto.Edutech_v1.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Edutech_v1.model.Incidencia;
import com.proyecto.Edutech_v1.model.Instructor;
import com.proyecto.Edutech_v1.model.LogicaSoporte;
import com.proyecto.Edutech_v1.repository.IncidenciaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class IncidenciaService {

    @Autowired
    private IncidenciaRepository incidenciaRepository;

    @Autowired
    private LogicaSoporteService soporteService;

    @Autowired
    private InstructorService instructorService;

    public Incidencia crearIncidenciaPorInstructor(Incidencia incidencia, Long instructorId) {
        Instructor instructor = instructorService.findById(instructorId);
        incidencia.setReportadoPorInstructor(instructor);
        incidencia.setFechaReporte(new Date());
        incidencia.setEstado("NUEVO");
        return incidenciaRepository.save(incidencia);
    }

    public Incidencia asignarIncidencia(Long incidenciaId, Long soporteId) {
        Incidencia incidencia = obtenerIncidenciaPorId(incidenciaId);
        LogicaSoporte soporte = soporteService.obtenerPorId(soporteId);
        incidencia.setAsignadoA(soporte);
        incidencia.setEstado("ASIGNADA");
        return incidenciaRepository.save(incidencia);
    }

    public Incidencia actualizarEstado(Long id, String nuevoEstado) {
        Incidencia incidencia = obtenerIncidenciaPorId(id);
        incidencia.setEstado(nuevoEstado);

        if ("RESUELTA".equalsIgnoreCase(nuevoEstado)) {
            incidencia.setFechaResolucion(new Date());
            if (incidencia.getAsignadoA() != null) {
                soporteService.actualizarIncidentes(
                        incidencia.getAsignadoA().getId(),
                        incidencia.getAsignadoA().getIncidentesResueltos() + 1);
            }
        } else {
            incidencia.setFechaResolucion(null);
        }

        return incidenciaRepository.save(incidencia);
    }

    public List<Incidencia> obtenerIncidenciasPorEstado(String estado) {
        return incidenciaRepository.findByEstado(estado);
    }


    public List<Incidencia> obtenerIncidenciasPorSoporte(Long soporteId) {
        return incidenciaRepository.findBySoporteAsignado(soporteId);
    }

    // Método para buscar incidencias por prioridad
    public Incidencia obtenerIncidenciaPorId(Long id) {
        return incidenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada con ID: " + id));
    }

    // Método para listar todas las incidencias
    public List<Incidencia> listarTodas() {
        return incidenciaRepository.findAll();
    }

    // Método para eliminar incidencias por estado
    public void eliminarIncidencia(Long id) {
        incidenciaRepository.deleteById(id);
    }
}