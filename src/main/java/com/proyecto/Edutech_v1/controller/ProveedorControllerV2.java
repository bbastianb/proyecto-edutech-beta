package com.proyecto.Edutech_v1.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.Edutech_v1.assamblers.ProveedorModelAssembler;
import com.proyecto.Edutech_v1.model.Proveedor;
import com.proyecto.Edutech_v1.service.ProveedorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorControllerV2 {
 @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private ProveedorModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Obtener todos los Proveedores", description = "Obtiene una lista de todos los proveedores registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Proveedor.class))),
                            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    public ResponseEntity<CollectionModel<EntityModel<Proveedor>>>listar() {
        List<EntityModel<Proveedor>> lista = proveedorService.listarTodos().stream()
        .map(assembler::toModel).collect(Collectors.toList());
        CollectionModel<EntityModel<Proveedor>> collectionModel = CollectionModel.of(lista)
            .add(linkTo(methodOn(ProveedorControllerV2.class).listar()).withSelfRel())
            .add(linkTo(methodOn(ProveedorControllerV2.class).guardar(null)).withRel("guardar"));
    
    return ResponseEntity.ok(collectionModel);
        
    }
    
@GetMapping("/listarPorId/{id}")
@Operation(summary = "Obtener los proveedores por id", description = "Obtiene el proveedores por id.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Proveedor.class))),
        @ApiResponse(responseCode = "404", description = "proveedor no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
})
public ResponseEntity<EntityModel<Proveedor>> obtenerProveedorPorId(@PathVariable Long id) {
    try {
        Proveedor lista = proveedorService.obtenerProveedorPorId(id);
        EntityModel<Proveedor> proveedorModel = assembler.toModel(lista);
        
        // Links adicionales específicos para este método
        proveedorModel.add(linkTo(methodOn(ProveedorControllerV2.class).obtenerProveedorPorId(id)).withSelfRel())
               .add(linkTo(methodOn(ProveedorControllerV2.class).listar()).withRel("todos-proveedor"))
               .add(linkTo(methodOn(ProveedorControllerV2.class).actualizar(id, null)).withRel("actualizar"))
               .add(linkTo(methodOn(ProveedorControllerV2.class).eliminar(id)).withRel("eliminar"));
    
    return ResponseEntity.ok(proveedorModel);
    } catch (RuntimeException e) {
    return ResponseEntity.notFound().build();
    }
}    
    @PostMapping("/guardar")//aprobado
    @Operation(summary = "Crear un nuevo proveedor", description = "Crea un nuevo proveedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Proveedor.class))),
                            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    public ResponseEntity<EntityModel<Proveedor>> guardar(@RequestBody Proveedor proveedor) {
        Proveedor nuevo = proveedorService.crearProveedor(proveedor);
        EntityModel<Proveedor> proveedorModel = EntityModel.of(nuevo);
        proveedorModel.add(linkTo(methodOn(ProveedorControllerV2.class).buscar(nuevo.getId())).withSelfRel());
        return ResponseEntity.ok(proveedorModel);
    }

    @GetMapping("/buscar/{id}")//aprobado
    @Operation(summary = "Obtener un proveedor por código", description = "Obtiene un proveedor por su código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Proveedor.class))),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    public ResponseEntity<EntityModel<Proveedor>> buscar(@PathVariable Long id) {
         try {
        Proveedor proveedor = proveedorService.obtenerProveedorPorId(id);
        EntityModel<Proveedor> proveedorModel=EntityModel.of(proveedor);
        proveedorModel.add(linkTo(methodOn(ProveedorControllerV2.class).buscar(id)).withSelfRel());
        return ResponseEntity.ok(proveedorModel);

    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
    
}

    @PutMapping("/actualizar/{id}")//aprobado
    @Operation(summary = "Actualizar un proveedor", description = "Actualiza un proveedor existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Proveedor.class))),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    public ResponseEntity<EntityModel<Proveedor>> actualizar(@PathVariable Long id, @RequestBody Proveedor proveedor) {
    try {
        Proveedor actualizado = proveedorService.actualizarProveedor(id, proveedor);
        EntityModel<Proveedor> proveedorModel=EntityModel.of(actualizado);
        proveedorModel.add(linkTo(WebMvcLinkBuilder.methodOn(ProveedorControllerV2.class).buscar(actualizado.getId())).withSelfRel());
        return ResponseEntity.ok(proveedorModel);
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}

    @DeleteMapping("/delete/{id}")//aprobado
    @Operation(summary = "Eliminar un proveedor ", description = "Elimina un proveedor por su código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrada")
    })
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            proveedorService.obtenerProveedorPorId(id); // verifica existencia
            proveedorService.eliminarProveedor(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
}

