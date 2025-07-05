package com.proyecto.Edutech_v1.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.Edutech_v1.assemblers.GerenteModelAssembler;
import com.proyecto.Edutech_v1.model.Gerente;
import com.proyecto.Edutech_v1.repository.GerenteRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Gestión de Gerentes V2", description = "API REST para gestión de gerentes con soporte HATEOAS")
@RestController
@RequestMapping("/api/v2/gerentes")
public class GerenteControllerV2 {

    private final GerenteRepository repository;
    private final GerenteModelAssembler assembler;

    public GerenteControllerV2(GerenteRepository repository, GerenteModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @Operation(summary = "Obtener todos los gerentes", description = "Retorna una lista de todos los gerentes con enlaces HATEOAS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = CollectionModel.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public CollectionModel<EntityModel<Gerente>> all() {
        List<EntityModel<Gerente>> gerentes = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(gerentes,
                linkTo(methodOn(GerenteControllerV2.class).all()).withSelfRel());
    }

    @Operation(summary = "Obtener gerente por ID", description = "Retorna un gerente específico con sus enlaces HATEOAS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gerente encontrado", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = EntityModel.class))),
            @ApiResponse(responseCode = "404", description = "Gerente no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public EntityModel<Gerente> one(
            @Parameter(description = "ID del gerente", required = true, example = "1") @PathVariable Long id) {
        Gerente gerente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gerente no encontrado con id " + id));

        return assembler.toModel(gerente);
    }
}