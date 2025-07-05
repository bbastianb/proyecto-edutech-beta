package com.proyecto.Edutech_v1.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.Edutech_v1.assemblers.CursoModelAssembler;
import com.proyecto.Edutech_v1.model.Curso;
import com.proyecto.Edutech_v1.service.CursoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Gestión de Cursos V2", description = "API REST para gestión de cursos con HATEOAS")
@RestController
@RequestMapping("/api/v2/cursos")
public class CursoControllerV2 {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CursoModelAssembler cursoAssembler;

    @Operation(summary = "Obtener todos los cursos", description = "Retorna todos los cursos con enlaces HATEOAS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = CollectionModel.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Curso>>> getAllCursos() {
        List<EntityModel<Curso>> cursos = cursoService.obtenerCursos().stream()
                .map(cursoAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(cursos, linkTo(methodOn(CursoControllerV2.class).getAllCursos()).withSelfRel()));
    }

    @Operation(summary = "Obtener curso por ID", description = "Retorna un curso específico con enlaces HATEOAS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso encontrado", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = EntityModel.class))),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Curso>> getCursoById(
            @Parameter(description = "ID del curso", required = true, example = "DEV-2023-01") @PathVariable String id) {
        Optional<Curso> cursoOpt = cursoService.buscarPorId(id);
        return cursoOpt.map(curso -> ResponseEntity.ok(cursoAssembler.toModel(curso)))
                .orElse(ResponseEntity.notFound().build());
    }
    

    @Operation(summary = "Inscribir estudiante en curso", description = "Inscribe un estudiante en un curso específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudiante inscrito correctamente"),
            @ApiResponse(responseCode = "400", description = "Estudiante ya inscrito o no existe"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @PostMapping("/{cursoId}/inscribir/{estudianteId}")
    public ResponseEntity<String> inscribirEstudiante(
            @Parameter(description = "ID del curso", required = true, example = "DEV-2023-01") @PathVariable String cursoId,
            @Parameter(description = "ID del estudiante", required = true, example = "1") @PathVariable Long estudianteId) {
        Optional<Curso> cursoOpt = cursoService.buscarPorId(cursoId);
        if (cursoOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Curso no encontrado");
        }
        boolean resultado = cursoService.inscribirEstudiantePorId(cursoOpt.get(), estudianteId);
        if (!resultado) {
            return ResponseEntity.badRequest().body("Estudiante ya inscrito o no existe");
        }
        return ResponseEntity.ok("Estudiante inscrito correctamente");
    }
}