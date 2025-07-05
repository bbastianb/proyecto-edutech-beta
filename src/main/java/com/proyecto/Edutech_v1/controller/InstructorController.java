package com.proyecto.Edutech_v1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.Edutech_v1.model.Instructor;
import com.proyecto.Edutech_v1.service.InstructorService;

@RestController
@RequestMapping("/api/instructores")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @GetMapping
    public List<Instructor> obtenerInstructores() {
        return instructorService.obtenerTodosLosInstructores();
        // Llama al método del servicio para obtener la lista de instructores
    }



    @PostMapping("/agregar")//aprobado
    public Instructor agregarInstructor(@RequestBody Instructor instructor) {
        return instructorService.guardarInstructor(instructor);
    }

    @PutMapping("/actualizar/{id}") //aprobado
    public Instructor actualizarInstructor(@PathVariable Long id, @RequestBody Instructor datosInstructor) {
        return instructorService.actualizarInstructor(id, datosInstructor);
    }

    @DeleteMapping("/eliminar/{id}")//aprobado
    public void eliminarInstructor(@PathVariable Long id) {
        instructorService.eliminarInstructor(id);
    }
}
