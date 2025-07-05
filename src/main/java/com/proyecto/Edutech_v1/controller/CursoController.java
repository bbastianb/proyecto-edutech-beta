package com.proyecto.Edutech_v1.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.Edutech_v1.model.Curso;
import com.proyecto.Edutech_v1.service.CursoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Gestión de Cursos", description = "Operaciones para la gestión de cursos en el sistema Edutech")
@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @Operation(summary = "Obtener todos los cursos", 
               description = "Retorna una lista completa de todos los cursos disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de cursos obtenida exitosamente",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Curso.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping()
    public ResponseEntity<List<Curso>> obtenerCursos() {
        return ResponseEntity.ok(cursoService.obtenerCursos());
    }

    @Operation(summary = "Asignar gerente a curso", 
               description = "Asigna un gerente existente a un curso específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Gerente asignado correctamente"),
        @ApiResponse(responseCode = "400", description = "El gerente no existe o ya está asignado"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{cursoId}/asignar-gerente/{gerenteId}")
    public ResponseEntity<String> asignarGerente(
            @Parameter(description = "ID del curso", required = true, example = "CUR001")
            @PathVariable String cursoId,
            @Parameter(description = "ID del gerente", required = true, example = "1")
            @PathVariable Long gerenteId) {

        Optional<Curso> cursoOpt = cursoService.buscarPorId(cursoId);
        if (cursoOpt.isEmpty()) {
            return ResponseEntity.status(404)
                    .body("No se asignó: No se encontró el curso con ID: " + cursoId);
        }

        boolean resultado = cursoService.asignarGerentePorId(cursoOpt.get(), gerenteId);
        if (!resultado) {
            return ResponseEntity.badRequest().body("El gerente no existe o ya está asignado a este curso.");
        }
        return ResponseEntity.ok("Gerente asignado correctamente al curso.");
    }

    @Operation(summary = "Asignar instructor a curso", 
               description = "Asigna un instructor existente a un curso específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Instructor asignado correctamente"),
        @ApiResponse(responseCode = "400", description = "El instructor no existe o ya está asignado"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{cursoId}/asignar-instructor/{instructorId}")
    public ResponseEntity<String> asignarInstructor(
            @Parameter(description = "ID del curso", required = true, example = "CUR001")
            @PathVariable String cursoId,
            @Parameter(description = "ID del instructor", required = true, example = "1")
            @PathVariable Long instructorId) {

        Optional<Curso> cursoOpt = cursoService.buscarPorId(cursoId);
        if (cursoOpt.isEmpty()) {
            return ResponseEntity.status(404)
                    .body("No se asignó: No se encontró el curso con ID: " + cursoId);
        }

        boolean resultado = cursoService.asignarInstructorPorId(cursoOpt.get(), instructorId);
        if (!resultado) {
            return ResponseEntity.badRequest().body("El instructor no existe o ya está asignado a este curso.");
        }
        return ResponseEntity.ok("Instructor asignado correctamente al curso.");
    }

    @Operation(summary = "Inscribir estudiante en curso", 
               description = "Inscribe un estudiante existente en un curso específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estudiante inscrito correctamente"),
        @ApiResponse(responseCode = "400", description = "El estudiante ya está inscrito o no existe"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{cursoId}/inscribir/{estudianteId}")
    public ResponseEntity<String> inscribirEstudiante(
            @Parameter(description = "ID del curso", required = true, example = "CUR001")
            @PathVariable String cursoId,
            @Parameter(description = "ID del estudiante", required = true, example = "1")
            @PathVariable Long estudianteId) {

        Optional<Curso> cursoOpt = cursoService.buscarPorId(cursoId);
        if (cursoOpt.isEmpty()) {
            return ResponseEntity.status(404)
                    .body("No se inscribió: No se encontró el curso con ID: " + cursoId);
        }

        boolean resultado = cursoService.inscribirEstudiantePorId(cursoOpt.get(), estudianteId);
        if (!resultado) {
            return ResponseEntity.badRequest().body("El estudiante ya está inscrito o no existe.");
        }
        return ResponseEntity.ok("Estudiante inscrito correctamente");
    }

    @Operation(summary = "Guardar nuevo curso", 
               description = "Crea y guarda un nuevo curso en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso guardado exitosamente",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Curso.class))),
        @ApiResponse(responseCode = "400", description = "Datos del curso inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/guardarcurso")
    public ResponseEntity<Curso> guardarCurso(
            @Parameter(description = "Datos del curso a guardar", required = true,
                      content = @Content(schema = @Schema(implementation = Curso.class)))
            @RequestBody Curso curso) {
        return ResponseEntity.ok(cursoService.guardarCurso(curso));
    }

    @Operation(summary = "Buscar curso por ID", 
               description = "Obtiene un curso específico según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso encontrado",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Curso.class))),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "ID del curso a buscar", required = true, example = "CUR001")
            @PathVariable String id) {
        Optional<Curso> cursoOpt = cursoService.buscarPorId(id);
        if (cursoOpt.isPresent()) {
            return ResponseEntity.ok(cursoOpt.get());
        } else {
            return ResponseEntity.status(404).body("El curso con ID " + id + " no fue encontrado.");
        }
    }
}