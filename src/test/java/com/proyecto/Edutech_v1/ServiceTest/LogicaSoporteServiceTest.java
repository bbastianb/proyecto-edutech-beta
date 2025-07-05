package com.proyecto.Edutech_v1.ServiceTest;

import com.proyecto.Edutech_v1.model.LogicaSoporte;
import com.proyecto.Edutech_v1.repository.LogicaSoporteRepository;
import com.proyecto.Edutech_v1.service.LogicaSoporteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogicaSoporteServiceTest {

    @Mock
    private LogicaSoporteRepository logicaSoporteRepository;

    @InjectMocks
    private LogicaSoporteService logicaSoporteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarTodos() {
        LogicaSoporte soporte = new LogicaSoporte();
        when(logicaSoporteRepository.findAll()).thenReturn(Arrays.asList(soporte));

        List<LogicaSoporte> lista = logicaSoporteService.listarTodos();

        assertEquals(1, lista.size());
        verify(logicaSoporteRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorIdExistente() {
        LogicaSoporte soporte = new LogicaSoporte();
        when(logicaSoporteRepository.findById(1L)).thenReturn(Optional.of(soporte));

        LogicaSoporte resultado = logicaSoporteService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(soporte, resultado);
    }

    @Test
    void testObtenerPorIdNoExistente() {
        when(logicaSoporteRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> logicaSoporteService.obtenerPorId(1L));

        assertTrue(ex.getMessage().contains("Soporte no encontrado"));
    }

    @Test
    void testGuardar() {
        LogicaSoporte soporte = new LogicaSoporte();
        when(logicaSoporteRepository.save(soporte)).thenReturn(soporte);

        LogicaSoporte guardado = logicaSoporteService.guardar(soporte);

        assertEquals(soporte, guardado);
        verify(logicaSoporteRepository).save(soporte);
    }

    @Test
    void testEliminar() {
        doNothing().when(logicaSoporteRepository).deleteById(1L);

        logicaSoporteService.eliminar(1L);

        verify(logicaSoporteRepository).deleteById(1L);
    }

    @Test
    void testBuscarPorTurno() {
        LogicaSoporte soporte = new LogicaSoporte();
        when(logicaSoporteRepository.findByTurno("Noche")).thenReturn(List.of(soporte));

        List<LogicaSoporte> resultado = logicaSoporteService.buscarPorTurno("Noche");

        assertFalse(resultado.isEmpty());
        verify(logicaSoporteRepository).findByTurno("Noche");
    }

    @Test
    void testBuscarPorMinIncidentes() {
        LogicaSoporte soporte = new LogicaSoporte();
        when(logicaSoporteRepository.findByIncidentesResueltosGreaterThanEqual(5))
                .thenReturn(List.of(soporte));

        List<LogicaSoporte> resultado = logicaSoporteService.buscarPorMinIncidentes(5);

        assertEquals(1, resultado.size());
        verify(logicaSoporteRepository).findByIncidentesResueltosGreaterThanEqual(5);
    }

    @Test
    void testActualizarIncidentes() {
        LogicaSoporte soporte = new LogicaSoporte();
        soporte.setIncidentesResueltos(3);

        when(logicaSoporteRepository.findById(1L)).thenReturn(Optional.of(soporte));
        when(logicaSoporteRepository.save(any(LogicaSoporte.class))).thenReturn(soporte);

        LogicaSoporte actualizado = logicaSoporteService.actualizarIncidentes(1L, 10);

        assertEquals(10, actualizado.getIncidentesResueltos());
        verify(logicaSoporteRepository).findById(1L);
        verify(logicaSoporteRepository).save(soporte);
    }

    @Test
    void testBuscarPorHerramienta() {
        LogicaSoporte soporte = new LogicaSoporte();
        when(logicaSoporteRepository.findByHerramientasSoporteContaining("ChatGPT"))
                .thenReturn(List.of(soporte));

        List<LogicaSoporte> resultado = logicaSoporteService.buscarPorHerramienta("ChatGPT");

        assertFalse(resultado.isEmpty());
        verify(logicaSoporteRepository).findByHerramientasSoporteContaining("ChatGPT");
    }
}
