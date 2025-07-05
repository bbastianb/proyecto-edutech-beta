package com.proyecto.Edutech_v1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.Edutech_v1.model.Incidencia;
import com.proyecto.Edutech_v1.service.IncidenciaService;

@RestController
@RequestMapping("/api/incidencias")
public class IncidenciaController {

    @Autowired
    private IncidenciaService incidenciaService;

    @PostMapping("/crear-por-instructor/{instructorId}") // Aprobado//hay campos que se aignan automaticamente
    public ResponseEntity<Incidencia> crearIncidenciaPorInstructor(
            @RequestBody Incidencia incidencia,
            @PathVariable Long instructorId) {
        Incidencia nueva = incidenciaService.crearIncidenciaPorInstructor(incidencia, instructorId);
        return ResponseEntity.ok(nueva);
    }

    @PutMapping("/{idIncidencia}/asignar/{idSoporte}") // Aprobado
    public ResponseEntity<Incidencia> asignarIncidencia(
            @PathVariable Long idIncidencia,
            @PathVariable Long idSoporte) {
        Incidencia incidencia = incidenciaService.asignarIncidencia(idIncidencia, idSoporte);
        return ResponseEntity.ok(incidencia);
    }

    @PutMapping("/{id}/actualizar-estado") // Aprobado
    public ResponseEntity<Incidencia> actualizarEstado(
            @PathVariable Long id,
            @RequestBody String estado) {
        Incidencia incidencia = incidenciaService.actualizarEstado(id, estado);
        return ResponseEntity.ok(incidencia);
    }

    @GetMapping
    public ResponseEntity<List<Incidencia>> listarTodas() {
        List<Incidencia> incidencias = incidenciaService.listarTodas();
        return ResponseEntity.ok(incidencias);
    }

    @GetMapping("/buscar/{id}")// Aprobado
    public ResponseEntity<Incidencia> obtenerPorId(@PathVariable Long id) {
        Incidencia incidencia = incidenciaService.obtenerIncidenciaPorId(id);
        return ResponseEntity.ok(incidencia);
    }

    @GetMapping("/estado/{estado}")// Aprobado
    public ResponseEntity<List<Incidencia>> obtenerPorEstado(@PathVariable String estado) {
        List<Incidencia> incidencias = incidenciaService.obtenerIncidenciasPorEstado(estado);
        return ResponseEntity.ok(incidencias);
    }

    @DeleteMapping("/eliminar/{id}")// Aprobado
    public ResponseEntity<Void> eliminarIncidencia(@PathVariable Long id) {
        incidenciaService.eliminarIncidencia(id);
        return ResponseEntity.noContent().build();
    }
}
