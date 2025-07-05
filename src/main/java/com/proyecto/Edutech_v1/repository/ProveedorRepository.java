package com.proyecto.Edutech_v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.proyecto.Edutech_v1.model.Proveedor;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    List<Proveedor> findByServicio(String servicio);

    List<Proveedor> findByTiempoRespuestaHorasLessThanEqual(Integer maxHoras);

    @Query("SELECT p FROM Proveedor p WHERE p.condicionesContrato >= :minCondicion")
    List<Proveedor> findByCondicionesContratoMinimas(Double minCondicion);

    @Query("SELECT p FROM Proveedor p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Proveedor> buscarPorNombreSimilar(String nombre);

    @Query("SELECT p.servicio, AVG(p.tiempoRespuestaHoras) FROM Proveedor p GROUP BY p.servicio")
    List<Object[]> findAvgTiempoRespuestaByServicio();
}
