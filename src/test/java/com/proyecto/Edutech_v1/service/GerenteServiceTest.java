package com.proyecto.Edutech_v1.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.proyecto.Edutech_v1.model.Gerente;
import com.proyecto.Edutech_v1.repository.GerenteRepository;

@ExtendWith(MockitoExtension.class)
public class GerenteServiceTest {

    @Mock
    private GerenteRepository gerenteRepository;

    @InjectMocks
    private GerenteService gerenteService;

    private Gerente gerente;

    @BeforeEach
    void setUp() {
        gerente = new Gerente();
        gerente.setIdGerente(1L);
        gerente.setNombreGere("Juan");
        gerente.setApellidoGere("Pérez");
        gerente.setEmailGere("juan@example.com");
        gerente.setContraseñaGere("password123");
        gerente.setTelefonoGere("123456789");
        gerente.setFechaRegistro(new Date());
        gerente.setEspecialidad("Tecnología Educativa");
        gerente.setCursosGestionados(5.0);
    }

    @Test
    void obtenerTodosLosGerentes_DebeRetornarLista() {
        
        List<Gerente> listaEsperada = Arrays.asList(gerente);
        when(gerenteRepository.findAll()).thenReturn(listaEsperada);

        List<Gerente> resultado = gerenteService.obtenerTodosLosGerentes();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombreGere());
        verify(gerenteRepository, times(1)).findAll();
    }

    @Test
    void guardarGerente_DebeRetornarGerenteGuardado() {

        when(gerenteRepository.save(any(Gerente.class))).thenReturn(gerente);

        Gerente resultado = gerenteService.guardarGerente(gerente);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombreGere());
        verify(gerenteRepository, times(1)).save(gerente);
    }

    @Test
    void eliminarGerente_DebeLlamarMetodoDelete() {

        doNothing().when(gerenteRepository).deleteById(1L);

        gerenteService.eliminarGerente(1L);

        verify(gerenteRepository, times(1)).deleteById(1L);
    }

    @Test
    void actualizarGerente_CuandoExiste_DebeActualizarDatos() {

        Gerente datosActualizados = new Gerente();
        datosActualizados.setNombreGere("Juan Actualizado");
        datosActualizados.setApellidoGere("Pérez Actualizado");
        datosActualizados.setEmailGere("juan.actualizado@example.com");
        datosActualizados.setContraseñaGere("nuevapassword");
        datosActualizados.setTelefonoGere("987654321");
        datosActualizados.setEspecialidad("Nueva Especialidad");
        datosActualizados.setCursosGestionados(10.0);

        when(gerenteRepository.findById(1L)).thenReturn(Optional.of(gerente));
        when(gerenteRepository.save(any(Gerente.class))).thenReturn(gerente);

        Gerente resultado = gerenteService.actualizarGerente(1L, datosActualizados);

        assertNotNull(resultado);
        assertEquals("Juan Actualizado", resultado.getNombreGere());
        assertEquals("Pérez Actualizado", resultado.getApellidoGere());
        assertEquals("juan.actualizado@example.com", resultado.getEmailGere());
        assertEquals(10.0, resultado.getCursosGestionados());
        verify(gerenteRepository, times(1)).findById(1L);
        verify(gerenteRepository, times(1)).save(gerente);
    }

    @Test
    void actualizarGerente_CuandoNoExiste_DebeLanzarExcepcion() {

        Gerente datosActualizados = new Gerente();
        when(gerenteRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            gerenteService.actualizarGerente(1L, datosActualizados);
        });

        assertEquals("Gerente no encontrado", exception.getMessage());
        verify(gerenteRepository, times(1)).findById(1L);
        verify(gerenteRepository, never()).save(any());
    }

    @Test
    void obtenerGerentePorId_CuandoExiste() {

        when(gerenteRepository.findById(1L)).thenReturn(Optional.of(gerente));

        Gerente resultado = gerenteRepository.findById(1L).orElseThrow();

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombreGere());
    }

    @Test
    void obtenerGerentePorId_CuandoNoExiste() {

        when(gerenteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            gerenteRepository.findById(99L).orElseThrow(() -> 
                new RuntimeException("Gerente no encontrado"));
        });
    }
}