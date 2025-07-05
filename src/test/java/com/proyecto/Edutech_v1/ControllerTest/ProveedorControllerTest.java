package com.proyecto.Edutech_v1.ControllerTest;



import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.Edutech_v1.assamblers.ProveedorModelAssembler;
import com.proyecto.Edutech_v1.controller.ProveedorControllerV2;
import com.proyecto.Edutech_v1.model.Proveedor;
import com.proyecto.Edutech_v1.service.ProveedorService;

@ExtendWith(MockitoExtension.class)
public class ProveedorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProveedorService proveedorService;

    @Mock
    private ProveedorModelAssembler assembler;

    @InjectMocks
    private ProveedorControllerV2 proveedorControllerv2;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Proveedor proveedor;
    private Proveedor proveedorActualizado;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(proveedorControllerv2).build();
        
        proveedor = new Proveedor();
        proveedor.setId(1L);
        proveedor.setNombre("Proveedor Test");
        proveedor.setServicio("Servicio de Limpieza");
        proveedor.setContacto("Juan Pérez - 123456789");
        proveedor.setDireccion("Av. Principal 123, Santiago");
        proveedor.setTiempoRespuestaHoras(24);
        proveedor.setCondicionesContrato("Pago a 30 días, garantía de 6 meses");

        proveedorActualizado = new Proveedor();
        proveedorActualizado.setId(1L);
        proveedorActualizado.setNombre("Proveedor Actualizado");
        proveedorActualizado.setServicio("Servicio de Mantenimiento");
        proveedorActualizado.setContacto("María González - 987654321");
        proveedorActualizado.setDireccion("Calle Nueva 456, Las Condes");
        proveedorActualizado.setTiempoRespuestaHoras(12);
        proveedorActualizado.setCondicionesContrato("Pago a 15 días, garantía de 12 meses");
    }

    @Test
    void testListarProveedores() throws Exception {
        List<Proveedor> proveedores = Arrays.asList(proveedor);
        when(proveedorService.listarTodos()).thenReturn(proveedores);

        mockMvc.perform(get("/api/proveedores")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(proveedorService, times(1)).listarTodos();
    }

@Test
void testListarProveedoresporId() throws Exception {
    when(proveedorService.obtenerProveedorPorId(1L)).thenReturn(proveedor);
    // Mock assembler to return an EntityModel wrapping the proveedor
    when(assembler.toModel(proveedor)).thenReturn(org.springframework.hateoas.EntityModel.of(proveedor));

    mockMvc.perform(get("/api/proveedores/listarPorId/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(proveedor.getId()))
                    .andExpect(jsonPath("$.nombre").value(proveedor.getNombre()))
                    .andExpect(jsonPath("$.servicio").value(proveedor.getServicio()))
                    .andExpect(jsonPath("$.contacto").value("Juan Pérez - 123456789"))
                    .andExpect(jsonPath("$.direccion").value("Av. Principal 123, Santiago"))
                    .andExpect(jsonPath("$.tiempoRespuestaHoras").value(24))
                    .andExpect(jsonPath("$.condicionesContrato").value("Pago a 30 días, garantía de 6 meses"));
    verify(proveedorService, times(1)).obtenerProveedorPorId(1L);
    verify(assembler, times(1)).toModel(proveedor);
} 
@Test
void testListarProveedoresporId_NoExiste() throws Exception {
    // Given
    when(proveedorService.obtenerProveedorPorId(999L))
            .thenThrow(new RuntimeException("Proveedor no encontrado"));

    // When & Then - Primero verificar que el endpoint existe
    mockMvc.perform(get("/api/proveedores/listarPorId/{id}", 999L)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

    // Verificar que el servicio SÍ fue llamado
    verify(proveedorService, times(1)).obtenerProveedorPorId(999L);
}

    @Test
    void testGuardarProveedor() throws Exception {
        when(proveedorService.crearProveedor(any(Proveedor.class))).thenReturn(proveedor);

        mockMvc.perform(post("/api/proveedores/guardar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proveedor)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(proveedor.getId()))
                .andExpect(jsonPath("$.nombre").value(proveedor.getNombre()))
                .andExpect(jsonPath("$.servicio").value(proveedor.getServicio()))
                .andExpect(jsonPath("$.contacto").value(proveedor.getContacto()))
                .andExpect(jsonPath("$.direccion").value(proveedor.getDireccion()))
                .andExpect(jsonPath("$.tiempoRespuestaHoras").value(proveedor.getTiempoRespuestaHoras()));

        verify(proveedorService, times(1)).crearProveedor(any(Proveedor.class));
    }

    @Test
    void testBuscarProveedorPorId() throws Exception {
        when(proveedorService.obtenerProveedorPorId(1L)).thenReturn(proveedor);

        mockMvc.perform(get("/api/proveedores/buscar/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(proveedor.getId()))
                .andExpect(jsonPath("$.nombre").value(proveedor.getNombre()))
                .andExpect(jsonPath("$.servicio").value(proveedor.getServicio()))
                .andExpect(jsonPath("$.contacto").value(proveedor.getContacto()));

        verify(proveedorService, times(1)).obtenerProveedorPorId(1L);
    }

    @Test
    void testBuscarProveedorPorId_NotFound() throws Exception {
        when(proveedorService.obtenerProveedorPorId(anyLong()))
                .thenThrow(new RuntimeException("Proveedor no encontrado"));

        mockMvc.perform(get("/api/proveedores/buscar/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(proveedorService, times(1)).obtenerProveedorPorId(999L);
    }

    @Test
    void testActualizarProveedor() throws Exception {
        when(proveedorService.actualizarProveedor(anyLong(), any(Proveedor.class)))
                .thenReturn(proveedorActualizado);

        mockMvc.perform(put("/api/proveedores/actualizar/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proveedorActualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(proveedorActualizado.getId()))
                .andExpect(jsonPath("$.nombre").value(proveedorActualizado.getNombre()))
                .andExpect(jsonPath("$.servicio").value(proveedorActualizado.getServicio()))
                .andExpect(jsonPath("$.contacto").value(proveedorActualizado.getContacto()))
                .andExpect(jsonPath("$.tiempoRespuestaHoras").value(proveedorActualizado.getTiempoRespuestaHoras()));

        verify(proveedorService, times(1)).actualizarProveedor(anyLong(), any(Proveedor.class));
    }

    @Test
    void testActualizarProveedor_NotFound() throws Exception {
        when(proveedorService.actualizarProveedor(anyLong(), any(Proveedor.class)))
                .thenThrow(new RuntimeException("Proveedor no encontrado"));

        mockMvc.perform(put("/api/proveedores/actualizar/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proveedorActualizado)))
                .andExpect(status().isNotFound());

        verify(proveedorService, times(1)).actualizarProveedor(anyLong(), any(Proveedor.class));
    }

    @Test
    void testEliminarProveedor() throws Exception {
        when(proveedorService.obtenerProveedorPorId(1L)).thenReturn(proveedor);

        mockMvc.perform(delete("/api/proveedores/delete/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(proveedorService, times(1)).obtenerProveedorPorId(1L);
        verify(proveedorService, times(1)).eliminarProveedor(1L);
    }

    @Test
    void testEliminarProveedor_NotFound() throws Exception {
        when(proveedorService.obtenerProveedorPorId(anyLong()))
                .thenThrow(new RuntimeException("Proveedor no encontrado"));

        mockMvc.perform(delete("/api/proveedores/delete/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(proveedorService, times(1)).obtenerProveedorPorId(999L);
        verify(proveedorService, times(0)).eliminarProveedor(anyLong());
    }

    @Test
    void testEliminarProveedor_ErrorDuranteEliminacion() throws Exception {
        when(proveedorService.obtenerProveedorPorId(1L)).thenReturn(proveedor);
        doThrow(new RuntimeException("Error al eliminar")).when(proveedorService).eliminarProveedor(1L);

        mockMvc.perform(delete("/api/proveedores/delete/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(proveedorService, times(1)).obtenerProveedorPorId(1L);
        verify(proveedorService, times(1)).eliminarProveedor(1L);
    }

    @Test
    void testGuardarProveedor_ConDatosVacios() throws Exception {
        Proveedor proveedorVacio = new Proveedor();
        when(proveedorService.crearProveedor(any(Proveedor.class))).thenReturn(proveedorVacio);

        mockMvc.perform(post("/api/proveedores/guardar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proveedorVacio)))
                .andExpect(status().isOk());

        verify(proveedorService, times(1)).crearProveedor(any(Proveedor.class));
    }

    @Test
    void testActualizarProveedor_ConDatosParciales() throws Exception {
        Proveedor proveedorParcial = new Proveedor();
        proveedorParcial.setId(1L);
        proveedorParcial.setNombre("Solo Nombre Actualizado");
        proveedorParcial.setServicio("Nuevo Servicio");
        proveedorParcial.setContacto("Contacto Actualizado");
        proveedorParcial.setDireccion("Nueva Dirección");
        proveedorParcial.setTiempoRespuestaHoras(48);
        proveedorParcial.setCondicionesContrato("Nuevas condiciones");
        
        when(proveedorService.actualizarProveedor(anyLong(), any(Proveedor.class)))
                .thenReturn(proveedorParcial);

        mockMvc.perform(put("/api/proveedores/actualizar/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proveedorParcial)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Solo Nombre Actualizado"))
                .andExpect(jsonPath("$.servicio").value("Nuevo Servicio"))
                .andExpect(jsonPath("$.tiempoRespuestaHoras").value(48));

        verify(proveedorService, times(1)).actualizarProveedor(anyLong(), any(Proveedor.class));
    }
}