package com.proyecto.Edutech_v1.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

import com.proyecto.Edutech_v1.assamblers.LogicaSoporteModelAssembler;
import com.proyecto.Edutech_v1.model.LogicaSoporte;
import com.proyecto.Edutech_v1.service.LogicaSoporteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/api/logica-soporte")
public class LogicaSoporteControllerV2 {
   @Autowired
    private LogicaSoporteModelAssembler assembler;

    @Autowired
    private LogicaSoporteService logicaSoporteService;

   @GetMapping
@Operation(summary = "Obtener todos los Reportes", description = "Obtiene una lista de todos los reportes registrados.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = LogicaSoporte.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
})
public ResponseEntity<CollectionModel<EntityModel<LogicaSoporte>>> listarTodos() {
    List<EntityModel<LogicaSoporte>> soportes = logicaSoporteService.listarTodos().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
    
    CollectionModel<EntityModel<LogicaSoporte>> collectionModel = CollectionModel.of(soportes)
            .add(linkTo(methodOn(LogicaSoporteControllerV2.class).listarTodos()).withSelfRel())
            .add(linkTo(methodOn(LogicaSoporteControllerV2.class).crear(null)).withRel("crear"));
    
    return ResponseEntity.ok(collectionModel);
}

@GetMapping("/listar/{id}")
@Operation(summary = "Obtener los Reporte por id", description = "Obtiene el reporte por id.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = LogicaSoporte.class))),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
})
public ResponseEntity<EntityModel<LogicaSoporte>> obtenerPorId(@PathVariable Long id) {
    try {
        LogicaSoporte soporte = logicaSoporteService.obtenerPorId(id);
        EntityModel<LogicaSoporte> soporteModel = assembler.toModel(soporte);
        
        // Links adicionales específicos para este método
        soporteModel.add(linkTo(methodOn(LogicaSoporteControllerV2.class).obtenerPorId(id)).withSelfRel())
                   .add(linkTo(methodOn(LogicaSoporteControllerV2.class).listarTodos()).withRel("todos-reportes"))
                   .add(linkTo(methodOn(LogicaSoporteControllerV2.class).actualizar(id, null)).withRel("actualizar"))
                   .add(linkTo(methodOn(LogicaSoporteControllerV2.class).eliminar(id)).withRel("eliminar"));
        
        return ResponseEntity.ok(soporteModel);
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}

@PostMapping("/crear")//aprobado
@Operation(summary = "Crear un nuevo reporte", description = "Crea un nuevo Reporte")
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reporte creada exitosamente",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = LogicaSoporte.class))),
                        @ApiResponse(responseCode = "400", description = "Datos del reporte inválidos",
                        content = @Content),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                        content = @Content)
})
public ResponseEntity<EntityModel<LogicaSoporte>> crear(@RequestBody LogicaSoporte soporte) {
    LogicaSoporte nuevoSoporte = logicaSoporteService.guardar(soporte);
    
    // Crear EntityModel con enlaces HATEOAS
    EntityModel<LogicaSoporte> soporteModel = EntityModel.of(nuevoSoporte);
    
    // Agregar enlaces HATEOAS
    soporteModel.add(linkTo(methodOn(LogicaSoporteControllerV2.class)
            .obtenerPorId(nuevoSoporte.getId())).withSelfRel());
    
    soporteModel.add(linkTo(methodOn(LogicaSoporteControllerV2.class)
            .actualizar(nuevoSoporte.getId(), null)).withRel("actualizar"));
    
    soporteModel.add(linkTo(methodOn(LogicaSoporteControllerV2.class)
            .eliminar(nuevoSoporte.getId())).withRel("eliminar"));
    
    soporteModel.add(linkTo(methodOn(LogicaSoporteControllerV2.class)
            .listarTodos()).withRel("todos-los-reportes"));
    
    return ResponseEntity.status(HttpStatus.CREATED).body(soporteModel);
}

@PutMapping("/actualizar/{id}")
@Operation(summary = "Actualizar un reporte", description = "Actualiza un reporte existente")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Reporte actualizado exitosamente",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = LogicaSoporte.class))),
    @ApiResponse(responseCode = "404", description = "Reporte no encontrado", content = @Content),
    @ApiResponse(responseCode = "400", description = "Datos del gerente inválidos", content = @Content),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
})
public ResponseEntity<EntityModel<LogicaSoporte>> actualizar(
        @PathVariable Long id,
        @RequestBody LogicaSoporte soporte) {
    try {
        LogicaSoporte existente = logicaSoporteService.obtenerPorId(id);

        // Actualizar campos
        existente.setTurno(soporte.getTurno());
        existente.setIncidentesResueltos(soporte.getIncidentesResueltos());
        existente.setHerramientasSoporte(soporte.getHerramientasSoporte());

        LogicaSoporte actualizado = logicaSoporteService.guardar(existente);

        EntityModel<LogicaSoporte> recurso = EntityModel.of(actualizado,
            linkTo(methodOn(LogicaSoporteControllerV2.class).actualizar(id, soporte)).withSelfRel(),
            linkTo(methodOn(LogicaSoporteControllerV2.class).obtenerPorId(id)).withRel("buscar"),
            linkTo(methodOn(LogicaSoporteControllerV2.class).listarTodos()).withRel("lista")
        );

        return ResponseEntity.ok(recurso);

    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}

    @DeleteMapping("/eliminar/{id}")//aprobado
    @Operation(summary = "Eliminar un Reporte  ", description = "Elimina un reporte por su código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reporte eliminada exitosamente",content = @Content),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrada",content = @Content),
            @ApiResponse(responseCode = "204", description = "Reporte eliminada exitosamente",content = @Content)
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            logicaSoporteService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }  
    
}