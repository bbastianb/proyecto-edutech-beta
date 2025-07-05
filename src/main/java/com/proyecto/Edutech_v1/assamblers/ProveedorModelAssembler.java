package com.proyecto.Edutech_v1.assamblers;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.proyecto.Edutech_v1.controller.ProveedorController;
import com.proyecto.Edutech_v1.model.Proveedor;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class ProveedorModelAssembler implements RepresentationModelAssembler<Proveedor, EntityModel<Proveedor>> {

    @Override
    public EntityModel<Proveedor> toModel(Proveedor proveedor) {
        return EntityModel.of(proveedor,
            linkTo(methodOn(ProveedorController.class).buscar(proveedor.getId())).withSelfRel(),
            linkTo(methodOn(ProveedorController.class).listar()).withRel("proveedores")
        );
    }
}


