package com.proyecto.Edutech_v1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.Edutech_v1.model.Gerente;
import com.proyecto.Edutech_v1.service.GerenteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Gestión de Gerentes", description = "Operaciones CRUD para la gestión de gerentes en el sistema Edutech")
@RestController
@RequestMapping("/api/gerentes")
public class GerenteController {

    @Autowired
    private GerenteService gerenteService;

    @Operation(summary = "Obtener todos los gerentes", description = "Retorna una lista completa de todos los gerentes registrados con sus cursos asignados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de gerentes obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Gerente.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping()
    public ResponseEntity<List<Gerente>> listarGerentes() {
        return ResponseEntity.ok(gerenteService.obtenerTodosLosGerentes());
    }

    @Operation(summary = "Agregar un nuevo gerente", description = "Registra un nuevo gerente en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Gerente creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Gerente.class))),
            @ApiResponse(responseCode = "400", description = "Datos del gerente inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping("/agregar")
    public ResponseEntity<Gerente> agregarGerente(
            @Parameter(description = "Objeto Gerente que necesita ser registrado", required = true, content = @Content(schema = @Schema(implementation = Gerente.class))) @RequestBody Gerente gerente) {
        return new ResponseEntity<>(gerenteService.guardarGerente(gerente), HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar un gerente", description = "Elimina un gerente del sistema según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Gerente eliminado exitosamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Gerente no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarGerente(
            @Parameter(description = "ID del gerente a eliminar", required = true, example = "1") @PathVariable Long id) {
        gerenteService.eliminarGerente(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar un gerente", description = "Actualiza la información de un gerente existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gerente actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Gerente.class))),
            @ApiResponse(responseCode = "404", description = "Gerente no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos del gerente inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Gerente> actualizarGerente(
            @Parameter(description = "ID del gerente a actualizar", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del gerente", required = true, content = @Content(schema = @Schema(implementation = Gerente.class))) @RequestBody Gerente datosGerente) {
        return ResponseEntity.ok(gerenteService.actualizarGerente(id, datosGerente));
    }
}