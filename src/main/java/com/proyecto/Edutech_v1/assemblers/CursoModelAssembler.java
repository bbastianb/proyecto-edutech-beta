package com.proyecto.Edutech_v1.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.proyecto.Edutech_v1.controller.CursoControllerV2;
import com.proyecto.Edutech_v1.model.Curso;



@Component
public class CursoModelAssembler implements RepresentationModelAssembler<Curso, EntityModel<Curso>> {

    @Override
    public EntityModel<Curso> toModel(Curso curso) {
        return EntityModel.of(curso,
            linkTo(methodOn(CursoControllerV2.class).getCursoById(curso.getCodigoCurso())).withSelfRel(),
            linkTo(methodOn(CursoControllerV2.class).getAllCursos()).withRel("cursos"),
            linkTo(methodOn(CursoControllerV2.class).inscribirEstudiante(curso.getCodigoCurso(), 0L)).withRel("inscribir-estudiante")
        );
    }
}
