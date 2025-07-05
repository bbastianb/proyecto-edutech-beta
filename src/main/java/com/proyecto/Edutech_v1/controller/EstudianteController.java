package com.proyecto.Edutech_v1.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.Edutech_v1.model.Estudiante;
import com.proyecto.Edutech_v1.service.EstudianteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Gestión de Estudiantes", description = "Operaciones CRUD para la gestión de estudiantes")
@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    EstudianteService estudianteService;

    @Operation(summary = "Obtener todos los estudiantes", description = "Retorna una lista de todos los estudiantes registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estudiantes obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Estudiante.class, type = "array"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping()
    public ArrayList<Estudiante> obtenerEstudiantes() {
        return estudianteService.obtenerEstudiantes();
    }

    @Operation(summary = "Crear nuevo estudiante", description = "Registra un nuevo estudiante en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudiante creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Estudiante.class))),
            @ApiResponse(responseCode = "400", description = "Datos del estudiante inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/guardar")
    public Estudiante guardarEstudiante(
            @Parameter(description = "Datos del estudiante a registrar", required = true, content = @Content(schema = @Schema(implementation = Estudiante.class))) @RequestBody Estudiante estudiante) {
        return this.estudianteService.guardarEstudiante(estudiante);
    }

    @Operation(summary = "Actualizar estudiante", description = "Actualiza los datos de un estudiante existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudiante actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Estudiante.class))),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/actualizar/{id}")
    public Estudiante actualizarEstudiante(
            @Parameter(description = "ID del estudiante a actualizar", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "Nuevos datos del estudiante", required = true, content = @Content(schema = @Schema(implementation = Estudiante.class))) @RequestBody Estudiante datosEstudiante) {
        return estudianteService.actualizarEstudiante(id, datosEstudiante);
    }

    @Operation(summary = "Eliminar estudiante", description = "Elimina un estudiante del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudiante eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public void eliminarEstudiante(
            @Parameter(description = "ID del estudiante a eliminar", required = true, example = "1") @PathVariable Long id) {
        estudianteService.eliminarEstudiante(id);
    }
}