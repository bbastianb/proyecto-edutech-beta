package com.proyecto.Edutech_v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.Edutech_v1.model.LogicaSoporte;

@Repository
public interface LogicaSoporteRepository extends JpaRepository<LogicaSoporte, Long> {

    // Método para buscar por turno
    List<LogicaSoporte> findByTurno(String turno);

    // Método para buscar por cantidad mínima de incidentes resueltos
    List<LogicaSoporte> findByIncidentesResueltosGreaterThanEqual(Integer minIncidentes);

    // Método para buscar por herramientas de soporte
    List<LogicaSoporte> findByHerramientasSoporteContaining(String herramienta);
}
