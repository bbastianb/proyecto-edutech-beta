package com.proyecto.Edutech_v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.Edutech_v1.model.Gerente;



public interface  GerenteRepository extends JpaRepository<Gerente, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar gerentes por especialidad o cursos gestionados

}
