package com.proyecto.Edutech_v1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Edutech_v1.model.Proveedor;
import com.proyecto.Edutech_v1.repository.ProveedorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProveedorService {

   
    @Autowired
    private ProveedorRepository proveedorRepository;

    public Proveedor crearProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public Proveedor actualizarProveedor(Long id, Proveedor proveedor) {
        Proveedor existente = obtenerProveedorPorId(id);
        existente.setNombre(proveedor.getNombre());
        existente.setServicio(proveedor.getServicio());
        existente.setContacto(proveedor.getContacto());
        existente.setDireccion(proveedor.getDireccion());
        existente.setTiempoRespuestaHoras(proveedor.getTiempoRespuestaHoras());
        existente.setCondicionesContrato(proveedor.getCondicionesContrato());
        return proveedorRepository.save(existente);
    }

    public void eliminarProveedor(Long id) {
        proveedorRepository.deleteById(id);
    }

    public Proveedor obtenerProveedorPorId(Long id) {
        return proveedorRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Proveedor no encontrado con ID: " + id));
    }

    public List<Proveedor> listarTodos() {
        return proveedorRepository.findAll();
    }

    public List<Proveedor> buscarPorServicio(String servicio) {
        return proveedorRepository.findByServicio(servicio);
    }

    public Proveedor actualizarCondicionesContrato(Long id, String nuevasCondiciones) {
        Proveedor proveedor = obtenerProveedorPorId(id);
        proveedor.setCondicionesContrato(nuevasCondiciones);
        return proveedorRepository.save(proveedor);
    }

}
