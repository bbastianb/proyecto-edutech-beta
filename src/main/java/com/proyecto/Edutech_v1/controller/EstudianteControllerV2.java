package com.proyecto.Edutech_v1.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.Edutech_v1.assemblers.EstudianteModelAssembler;
import com.proyecto.Edutech_v1.model.Estudiante;
import com.proyecto.Edutech_v1.service.EstudianteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Gestión de Estudiantes V2", description = "API versionada para la gestión de estudiantes con HATEOAS")
@RestController
@RequestMapping("/api/v2/estudiantes")
public class EstudianteControllerV2 {

    private final EstudianteService estudianteService;
    private final EstudianteModelAssembler assembler;

    public EstudianteControllerV2(EstudianteService estudianteService, EstudianteModelAssembler assembler) {
        this.estudianteService = estudianteService;
        this.assembler = assembler;
    }

    @Operation(summary = "Obtener todos los estudiantes",
               description = "Retorna todos los estudiantes con enlaces HATEOAS")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de estudiantes obtenida exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Estudiante.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public CollectionModel<EntityModel<Estudiante>> all() {
        List<EntityModel<Estudiante>> estudiantes = estudianteService.obtenerEstudiantes().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(estudiantes,
            linkTo(methodOn(EstudianteControllerV2.class).all()).withSelfRel());
    }

    @Operation(summary = "Obtener estudiante por ID",
               description = "Retorna un estudiante específico con enlaces HATEOAS")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estudiante encontrado",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Estudiante.class))),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Estudiante>> one(
            @Parameter(description = "ID del estudiante a obtener", required = true, example = "1")
            @PathVariable Long id) {
        Estudiante estudiante = estudianteService.obtenerEstudiantes().stream()
            .filter(e -> e.getIdEstudiante().equals(id))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        return ResponseEntity.ok(assembler.toModel(estudiante));
    }

    @Operation(summary = "Crear nuevo estudiante",
               description = "Guarda un nuevo estudiante en el sistema y retorna el recurso creado con enlaces HATEOAS")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Estudiante creado exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Estudiante.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EntityModel<Estudiante>> create(
            @Parameter(description = "Objeto Estudiante a crear", required = true, content = @Content(schema = @Schema(implementation = Estudiante.class)))
            @RequestBody Estudiante nuevoEstudiante) {
        Estudiante saved = estudianteService.guardarEstudiante(nuevoEstudiante);
        return ResponseEntity
                .created(linkTo(methodOn(EstudianteControllerV2.class).one(saved.getIdEstudiante())).toUri())
                .body(assembler.toModel(saved));
    }

    @Operation(summary = "Actualizar estudiante existente",
               description = "Actualiza un estudiante existente y retorna el recurso actualizado con enlaces HATEOAS")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estudiante actualizado exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Estudiante.class))),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Estudiante>> update(
            @Parameter(description = "ID del estudiante a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del estudiante", required = true, content = @Content(schema = @Schema(implementation = Estudiante.class)))
            @RequestBody Estudiante datosEstudiante) {
        Estudiante actualizado = estudianteService.actualizarEstudiante(id, datosEstudiante);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @Operation(summary = "Eliminar estudiante",
               description = "Elimina un estudiante según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Estudiante eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID del estudiante a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        estudianteService.eliminarEstudiante(id);
        return ResponseEntity.noContent().build();
    }
}
