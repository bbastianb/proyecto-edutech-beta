package com.proyecto.Edutech_v1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Edutech_v1.model.LogicaSoporte;
import com.proyecto.Edutech_v1.repository.LogicaSoporteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LogicaSoporteService {
    @Autowired
    private LogicaSoporteRepository logicaSoporteRepository;

    public List<LogicaSoporte> listarTodos() {
        return logicaSoporteRepository.findAll();
    }

    public LogicaSoporte obtenerPorId(Long id) {
        return logicaSoporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Soporte no encontrado con ID: " + id));
    }

    public LogicaSoporte guardar(LogicaSoporte soporte) {
        return logicaSoporteRepository.save(soporte);
    }

    public void eliminar(Long id) {
        logicaSoporteRepository.deleteById(id);
    }

    public List<LogicaSoporte> buscarPorTurno(String turno) {
        return logicaSoporteRepository.findByTurno(turno);
    }

    public List<LogicaSoporte> buscarPorMinIncidentes(Integer minIncidentes) {
        return logicaSoporteRepository.findByIncidentesResueltosGreaterThanEqual(minIncidentes);
    }

    public LogicaSoporte actualizarIncidentes(Long id, Integer nuevosIncidentes) {
        LogicaSoporte soporte = obtenerPorId(id);
        soporte.setIncidentesResueltos(nuevosIncidentes);
        return guardar(soporte);
    }

    public List<LogicaSoporte> buscarPorHerramienta(String herramienta) {
        return logicaSoporteRepository.findByHerramientasSoporteContaining(herramienta);
    }

}
