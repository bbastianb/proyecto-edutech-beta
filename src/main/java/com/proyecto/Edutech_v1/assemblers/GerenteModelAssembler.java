package com.proyecto.Edutech_v1.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.proyecto.Edutech_v1.controller.GerenteControllerV2;
import com.proyecto.Edutech_v1.model.Gerente;



@Component
public class GerenteModelAssembler implements RepresentationModelAssembler<Gerente, EntityModel<Gerente>> {

    @Override
    public EntityModel<Gerente> toModel(Gerente gerente) {
        return EntityModel.of(gerente,
                linkTo(methodOn(GerenteControllerV2.class).one(gerente.getIdGerente())).withSelfRel(),
                linkTo(methodOn(GerenteControllerV2.class).all()).withRel("gerentes"));
    }

}
