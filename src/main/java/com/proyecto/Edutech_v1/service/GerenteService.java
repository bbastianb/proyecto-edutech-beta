package com.proyecto.Edutech_v1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Edutech_v1.model.Gerente;
import com.proyecto.Edutech_v1.repository.GerenteRepository;

@Service
public class GerenteService {

    @Autowired
    private GerenteRepository gerenteRepository;

    public List<Gerente> obtenerTodosLosGerentes() {
        return gerenteRepository.findAll();
    }

    public Gerente guardarGerente(Gerente gerente) {
        return gerenteRepository.save(gerente);
    }

    public void eliminarGerente(Long id) {
        gerenteRepository.deleteById(id);
    }

    public Gerente actualizarGerente(Long id, Gerente datosGerente) {
        Gerente gerente = gerenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gerente no encontrado"));
        gerente.setNombreGere(datosGerente.getNombreGere());
        gerente.setApellidoGere(datosGerente.getApellidoGere());
        gerente.setEmailGere(datosGerente.getEmailGere());
        gerente.setContraseñaGere(datosGerente.getContraseñaGere());
        gerente.setTelefonoGere(datosGerente.getTelefonoGere());
        gerente.setEspecialidad(datosGerente.getEspecialidad());
        gerente.setCursosGestionados(datosGerente.getCursosGestionados());
        // No se puede actualizar el ID ni la fecha de registro
        return gerenteRepository.save(gerente);
    }

}
