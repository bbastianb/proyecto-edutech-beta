package com.proyecto.Edutech_v1.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.proyecto.Edutech_v1.controller.EstudianteControllerV2;
import com.proyecto.Edutech_v1.model.Estudiante;




@Component
public class EstudianteModelAssembler implements RepresentationModelAssembler<Estudiante, EntityModel<Estudiante>> {

    @Override
    public EntityModel<Estudiante> toModel(Estudiante estudiante) {
        return EntityModel.of(estudiante,
                linkTo(methodOn(EstudianteControllerV2.class).one(estudiante.getIdEstudiante())).withSelfRel(),
                linkTo(methodOn(EstudianteControllerV2.class).all()).withRel("estudiantes"));
    }

}
