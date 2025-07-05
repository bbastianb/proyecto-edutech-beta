package com.proyecto.Edutech_v1.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.Edutech_v1.assamblers.LogicaSoporteModelAssembler;
import com.proyecto.Edutech_v1.controller.LogicaSoporteControllerV2;
import com.proyecto.Edutech_v1.model.LogicaSoporte;
import com.proyecto.Edutech_v1.service.LogicaSoporteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.EntityModel;

import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(MockitoExtension.class)
class LogicaSoporteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LogicaSoporteService logicaSoporteService;

    @Mock
    private LogicaSoporteModelAssembler assembler;
    @InjectMocks
    private LogicaSoporteControllerV2 LogicaSoporteControllerv2;
   
    private ObjectMapper objectMapper = new ObjectMapper();

    private LogicaSoporte soporteEjemplo;
    private LogicaSoporte soporteEjemplo2;
    private LogicaSoporte soporteEjemplo3;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(LogicaSoporteControllerv2).build();
        soporteEjemplo = new LogicaSoporte();
        soporteEjemplo.setId(1L);
        soporteEjemplo.setTurno("Mañana");
        soporteEjemplo.setIncidentesResueltos(15);
        soporteEjemplo.setHerramientasSoporte("ServiceNow, Jira");

        soporteEjemplo2 = new LogicaSoporte();
        soporteEjemplo2.setId(2L);
        soporteEjemplo2.setTurno("Tarde");
        soporteEjemplo2.setIncidentesResueltos(12);
        soporteEjemplo2.setHerramientasSoporte("Zendesk, Slack");

        soporteEjemplo3 = new LogicaSoporte();
        soporteEjemplo3.setId(3L);
        soporteEjemplo3.setTurno("Noche");
        soporteEjemplo3.setIncidentesResueltos(8);
        soporteEjemplo3.setHerramientasSoporte("PagerDuty, Opsgenie");
    }

    // ==================== TESTS PARA LISTAR TODOS ====================

    @Test
    void listarTodos() throws Exception {
        // Arrange
        List<LogicaSoporte> listaSoportes = Arrays.asList(soporteEjemplo, soporteEjemplo2, soporteEjemplo3);
        when(logicaSoporteService.listarTodos()).thenReturn(listaSoportes);

        // Act & Assert
        mockMvc.perform(get("/api/logica-soporte")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(logicaSoporteService, times(1)).listarTodos();
    }


    // ==================== TESTS PARA OBTENER POR ID ====================

    @Test
    void testObtenerSoportePorId() throws Exception {
        // Given
        when(logicaSoporteService.obtenerPorId(1L)).thenReturn(soporteEjemplo);
        when(assembler.toModel(soporteEjemplo)).thenReturn(EntityModel.of(soporteEjemplo));

        // When & Then
        mockMvc.perform(get("/api/logica-soporte/listar/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value(1))
                        .andExpect(jsonPath("$.turno").value("Mañana"))
                        .andExpect(jsonPath("$.incidentesResueltos").value(15))
                        .andExpect(jsonPath("$.herramientasSoporte").value("ServiceNow, Jira"));

        verify(logicaSoporteService, times(1)).obtenerPorId(1L);
        verify(assembler, times(1)).toModel(soporteEjemplo);
    }

    @Test
    void obtenerPorId_DeberiaRetornar404_CuandoIdNoExiste() throws Exception {
        // Arrange
        Long id = 999L;
        when(logicaSoporteService.obtenerPorId(id))
                .thenThrow(new RuntimeException("Soporte no encontrado con ID: " + id));

        // Act & Assert
        mockMvc.perform(get("/api/logica-soporte/listar/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(logicaSoporteService, times(1)).obtenerPorId(id);
    }

    @Test
    void obtenerPorId_DeberiaRetornar404_CuandoIdEsCero() throws Exception {
        // Arrange
        Long id = 0L;
        when(logicaSoporteService.obtenerPorId(id))
                .thenThrow(new RuntimeException("Soporte no encontrado con ID: " + id));

        // Act & Assert
        mockMvc.perform(get("/api/logica-soporte/listar/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(logicaSoporteService, times(1)).obtenerPorId(id);
    }

    @Test
    void obtenerPorId_DeberiaRetornar404_CuandoIdEsNegativo() throws Exception {
        // Arrange
        Long id = -1L;
        when(logicaSoporteService.obtenerPorId(id))
                .thenThrow(new RuntimeException("Soporte no encontrado con ID: " + id));

        // Act & Assert
        mockMvc.perform(get("/api/logica-soporte/listar/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(logicaSoporteService, times(1)).obtenerPorId(id);
    }

    // ==================== TESTS PARA CREAR ====================

    @Test
    void crear_DeberiaCrearReporte_CuandoDatosSonValidos() throws Exception {
        // Arrange
        LogicaSoporte nuevoSoporte = new LogicaSoporte();
        nuevoSoporte.setTurno("Noche");
        nuevoSoporte.setIncidentesResueltos(8);
        nuevoSoporte.setHerramientasSoporte("PagerDuty, Opsgenie");

        LogicaSoporte soporteGuardado = new LogicaSoporte();
        soporteGuardado.setId(3L);
        soporteGuardado.setTurno("Noche");
        soporteGuardado.setIncidentesResueltos(8);
        soporteGuardado.setHerramientasSoporte("PagerDuty, Opsgenie");

        when(logicaSoporteService.guardar(any(LogicaSoporte.class))).thenReturn(soporteGuardado);

        String jsonContent = objectMapper.writeValueAsString(nuevoSoporte);

        // Act & Assert
        mockMvc.perform(post("/api/logica-soporte/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isCreated());

        verify(logicaSoporteService, times(1)).guardar(any(LogicaSoporte.class));
    }

    @Test
    void crear_DeberiaRetornar400_CuandoJsonEsInvalido() throws Exception {
        // Arrange
        String jsonInvalido = "{invalid json}";

        // Act & Assert
        mockMvc.perform(post("/api/logica-soporte/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());

        verify(logicaSoporteService, never()).guardar(any(LogicaSoporte.class));
    }

    
    // ==================== TESTS PARA ACTUALIZAR ====================

    @Test
    void actualizar_DeberiaActualizarReporte_CuandoIdExisteYDatosSonValidos() throws Exception {
        // Arrange
        Long id = 1L;
        LogicaSoporte soporteActualizado = new LogicaSoporte();
        soporteActualizado.setTurno("Tarde");
        soporteActualizado.setIncidentesResueltos(20);
        soporteActualizado.setHerramientasSoporte("ServiceNow, Zendesk, Jira");

        when(logicaSoporteService.obtenerPorId(id)).thenReturn(soporteEjemplo);
        when(logicaSoporteService.guardar(any(LogicaSoporte.class))).thenReturn(soporteEjemplo);

        String jsonContent = objectMapper.writeValueAsString(soporteActualizado);

        // Act & Assert
        mockMvc.perform(put("/api/logica-soporte/actualizar/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk());

        verify(logicaSoporteService, times(1)).obtenerPorId(id);
        verify(logicaSoporteService, times(1)).guardar(any(LogicaSoporte.class));
    }

    @Test
    void actualizar_DeberiaRetornar404_CuandoIdNoExiste() throws Exception {
        // Arrange
        Long id = 999L;
        LogicaSoporte soporteActualizado = new LogicaSoporte();
        soporteActualizado.setTurno("Noche");
        soporteActualizado.setIncidentesResueltos(5);
        soporteActualizado.setHerramientasSoporte("Slack");

        when(logicaSoporteService.obtenerPorId(id))
                .thenThrow(new RuntimeException("Soporte no encontrado con ID: " + id));

        String jsonContent = objectMapper.writeValueAsString(soporteActualizado);

        // Act & Assert
        mockMvc.perform(put("/api/logica-soporte/actualizar/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isNotFound());

        verify(logicaSoporteService, times(1)).obtenerPorId(id);
        verify(logicaSoporteService, never()).guardar(any(LogicaSoporte.class));
    }

   
    @Test
    void actualizar_DeberiaManteneerCamposExistentes_CuandoAlgunosCamposSonNulosEnRequest() throws Exception {
        // Arrange
        Long id = 1L;
        LogicaSoporte requestActualizacion = new LogicaSoporte();
        requestActualizacion.setTurno("Madrugada");
        // incidentesResueltos y herramientasSoporte quedan null en el request
        // pero el controller debería mantener los valores existentes

        LogicaSoporte soporteExistente = new LogicaSoporte();
        soporteExistente.setId(1L);
        soporteExistente.setTurno("Mañana");
        soporteExistente.setIncidentesResueltos(15);
        soporteExistente.setHerramientasSoporte("ServiceNow, Jira");

        when(logicaSoporteService.obtenerPorId(id)).thenReturn(soporteExistente);
        when(logicaSoporteService.guardar(any(LogicaSoporte.class))).thenReturn(soporteExistente);

        String jsonContent = objectMapper.writeValueAsString(requestActualizacion);

        // Act & Assert
        mockMvc.perform(put("/api/logica-soporte/actualizar/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk());

        verify(logicaSoporteService, times(1)).obtenerPorId(id);
        verify(logicaSoporteService, times(1)).guardar(any(LogicaSoporte.class));
    }

  

    // ==================== TESTS PARA ELIMINAR ====================

    @Test
    void eliminar_DeberiaEliminarReporte_CuandoIdExiste() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(logicaSoporteService).eliminar(id);

        // Act & Assert
        mockMvc.perform(delete("/api/logica-soporte/eliminar/{id}", id))
                .andExpect(status().isNoContent());

        verify(logicaSoporteService, times(1)).eliminar(id);
    }

    @Test
    void eliminar_DeberiaRetornar404_CuandoIdNoExiste() throws Exception {
        // Arrange
        Long id = 999L;
        doThrow(new EmptyResultDataAccessException(1)).when(logicaSoporteService).eliminar(id);

        // Act & Assert
        mockMvc.perform(delete("/api/logica-soporte/eliminar/{id}", id))
                .andExpect(status().isNotFound());

        verify(logicaSoporteService, times(1)).eliminar(id);
    }
}
    