package com.proyecto.Edutech_v1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.Edutech_v1.model.Proveedor;
import com.proyecto.Edutech_v1.service.ProveedorService;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {
    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<Proveedor>> listar() {
        List<Proveedor> lista = proveedorService.listarTodos();
        return lista.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(lista);
    }

    @PostMapping("/guardar")//aprobado
    public ResponseEntity<Proveedor> guardar(@RequestBody Proveedor proveedor) {
        Proveedor nuevo = proveedorService.crearProveedor(proveedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping("/buscar/{id}")//aprobado
    public ResponseEntity<Proveedor> buscar(@PathVariable Long id) {
         try {
        Proveedor proveedor = proveedorService.obtenerProveedorPorId(id);
        return ResponseEntity.ok(proveedor);
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}

    @PutMapping("/actualizar/{id}")//aprobado
    public ResponseEntity<Proveedor> actualizar(@PathVariable Long id, @RequestBody Proveedor proveedor) {
    try {
        Proveedor actualizado = proveedorService.actualizarProveedor(id, proveedor);
        return ResponseEntity.ok(actualizado);
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}

    @DeleteMapping("/delete/{id}")//aprobado
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            proveedorService.obtenerProveedorPorId(id); // verifica existencia
            proveedorService.eliminarProveedor(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

