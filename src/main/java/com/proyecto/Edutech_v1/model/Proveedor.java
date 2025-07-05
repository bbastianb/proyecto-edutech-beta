package com.proyecto.Edutech_v1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "proveedor")
@Data
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "nombre_proveedor" ,nullable = false, length = 100)
    private String nombre;

    @Column(name= "servicio",nullable = false, length = 100)
    private String servicio;

    @Column(name= "contacto",nullable = false, length = 100)
    private String contacto;

    @Column(name= "direccion" ,nullable = false, length = 200)
    private String direccion;

    @Column(name = "tiempo_respuesta_horas", nullable = false)
    private Integer tiempoRespuestaHoras;

    @Column(name = "condiciones_contrato", nullable = false, length = 500)
    private String condicionesContrato;


    // Métodos específicos de negocio
    public void actualizarTiempoRespuesta(Integer nuevoTiempo) {
        this.tiempoRespuestaHoras = nuevoTiempo;
    }

    public void renegociarContrato(String nuevasCondiciones) {
        this.condicionesContrato = nuevasCondiciones;
    }

    public String generarReporteDesempeno() {
        return String.format("Proveedor: %s | Servicio: %s | Tiempo Respuesta: %d hrs | Condiciones: %s",
                nombre, servicio, tiempoRespuestaHoras, condicionesContrato);
    }
    

}
