package com.proyecto.Edutech_v1.assamblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.proyecto.Edutech_v1.controller.LogicaSoporteController;
import com.proyecto.Edutech_v1.model.LogicaSoporte;
@Component
public class LogicaSoporteModelAssembler implements RepresentationModelAssembler<LogicaSoporte, EntityModel<LogicaSoporte>> {

    @Override
    public EntityModel<LogicaSoporte> toModel(LogicaSoporte logicaSoporte) {
        return EntityModel.of(logicaSoporte,
            linkTo(methodOn(LogicaSoporteController.class).obtenerPorId(logicaSoporte.getId())).withSelfRel(),
            linkTo(methodOn(LogicaSoporteController.class).listarTodos()).withRel("logicas")
        );
    }

}
