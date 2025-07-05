package com.proyecto.Edutech_v1.ServiceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.proyecto.Edutech_v1.model.Proveedor;
import com.proyecto.Edutech_v1.repository.ProveedorRepository;
import com.proyecto.Edutech_v1.service.ProveedorService;

@ExtendWith(MockitoExtension.class)
class ProveedorServiceTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorService proveedorService;

    private Proveedor proveedor;
    private final Long PROVEEDOR_ID = 1L;

    @BeforeEach
    void setUp() {
        proveedor = new Proveedor();
        proveedor.setId(PROVEEDOR_ID);
        proveedor.setNombre("Proveedor Test");
        proveedor.setServicio("Servicio Educativo");
        proveedor.setContacto("contacto@test.com");
        proveedor.setDireccion("Calle Test 123");
        proveedor.setTiempoRespuestaHoras(24);
        proveedor.setCondicionesContrato("Condiciones iniciales");
    }

    @Test
    void guardarProveedor() {
        // Prepara
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedor);

        // Actua
        Proveedor resultado = proveedorService.crearProveedor(proveedor);

        // Verifica
        assertNotNull(resultado);
        assertEquals(proveedor.getId(), resultado.getId());
        assertEquals(proveedor.getNombre(), resultado.getNombre());
        assertEquals(proveedor.getServicio(), resultado.getServicio());
        assertEquals(proveedor.getContacto(), resultado.getContacto());
        assertEquals(proveedor.getDireccion(), resultado.getDireccion());
        assertEquals(proveedor.getTiempoRespuestaHoras(), resultado.getTiempoRespuestaHoras());
        assertEquals(proveedor.getCondicionesContrato(), resultado.getCondicionesContrato());
        verify(proveedorRepository).save(proveedor);
    }

    @Test
    void actualizarProveedor_ShouldUpdateExistingProveedor() {
        // Arrange
        Proveedor proveedorActualizado = new Proveedor();
        proveedorActualizado.setNombre("Proveedor Actualizado");
        proveedorActualizado.setServicio("Nuevo Servicio");
        proveedorActualizado.setContacto("nuevo@contacto.com");
        proveedorActualizado.setDireccion("Nueva Dirección 456");
        proveedorActualizado.setTiempoRespuestaHoras(48);
        proveedorActualizado.setCondicionesContrato("Nuevas condiciones");

        when(proveedorRepository.findById(PROVEEDOR_ID)).thenReturn(Optional.of(proveedor));
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedorActualizado);

        // Act
        Proveedor resultado = proveedorService.actualizarProveedor(PROVEEDOR_ID, proveedorActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals(proveedorActualizado.getId(), resultado.getId());
        assertEquals(proveedorActualizado.getNombre(), resultado.getNombre());
        assertEquals(proveedorActualizado.getServicio(), resultado.getServicio());
        assertEquals(proveedorActualizado.getContacto(), resultado.getContacto());
        assertEquals(proveedorActualizado.getDireccion(), resultado.getDireccion());
        assertEquals(proveedorActualizado.getTiempoRespuestaHoras(), resultado.getTiempoRespuestaHoras());
        assertEquals(proveedorActualizado.getCondicionesContrato(), resultado.getCondicionesContrato());
        verify(proveedorRepository).findById(PROVEEDOR_ID);
        verify(proveedorRepository).save(proveedor);
    }

    @Test
    void actualizarProveedor_NoExiste() {
        // Arrange
        when(proveedorRepository.findById(PROVEEDOR_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            proveedorService.actualizarProveedor(PROVEEDOR_ID, proveedor);
        });
        verify(proveedorRepository).findById(PROVEEDOR_ID);
        verify(proveedorRepository, never()).save(any());
    }

    @Test
    void eliminarProveedor_ShouldDeleteProveedor() {
        // Arrange
        doNothing().when(proveedorRepository).deleteById(PROVEEDOR_ID);

        // Act
        proveedorService.eliminarProveedor(PROVEEDOR_ID);

        // Assert
        verify(proveedorRepository).deleteById(PROVEEDOR_ID);
    }

    @Test
    void obtenerProveedorPorId_ShouldReturnProveedorWhenExists() {
        // Arrange
        when(proveedorRepository.findById(PROVEEDOR_ID)).thenReturn(Optional.of(proveedor));

        // Act
        Proveedor resultado = proveedorService.obtenerProveedorPorId(PROVEEDOR_ID);

        // Assert
        assertNotNull(resultado);
        assertEquals(proveedor.getId(), resultado.getId());
        verify(proveedorRepository).findById(PROVEEDOR_ID);
    }

    @Test
    void obtenerProveedorPorId_ShouldThrowExceptionWhenProveedorNotFound() {
        // Arrange
        when(proveedorRepository.findById(PROVEEDOR_ID)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            proveedorService.obtenerProveedorPorId(PROVEEDOR_ID);
        });

        assertEquals("Proveedor no encontrado con ID: " + PROVEEDOR_ID, exception.getMessage());
        verify(proveedorRepository).findById(PROVEEDOR_ID);
    }

    @Test
    void listarTodos_ShouldReturnAllProveedores() {
        // Arrange
        List<Proveedor> proveedores = Arrays.asList(proveedor);
        when(proveedorRepository.findAll()).thenReturn(proveedores);

        // Act
        List<Proveedor> resultado = proveedorService.listarTodos();

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(proveedorRepository).findAll();
    }

    @Test
    void buscarPorServicio_ShouldReturnProveedoresByServicio() {
        // Arrange
        String servicio = "Servicio Educativo";
        List<Proveedor> proveedores = Arrays.asList(proveedor);
        when(proveedorRepository.findByServicio(servicio)).thenReturn(proveedores);

        // Act
        List<Proveedor> resultado = proveedorService.buscarPorServicio(servicio);

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(servicio, resultado.get(0).getServicio());
        verify(proveedorRepository).findByServicio(servicio);
    }

    @Test
    void actualizarCondicionesContrato_ShouldUpdateCondicionesContrato() {
        // Arrange
        String nuevasCondiciones = "Nuevas condiciones de contrato";
        when(proveedorRepository.findById(PROVEEDOR_ID)).thenReturn(Optional.of(proveedor));
        when(proveedorRepository.save(any(Proveedor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Proveedor resultado = proveedorService.actualizarCondicionesContrato(PROVEEDOR_ID, nuevasCondiciones);

        // Assert
        assertNotNull(resultado);
        assertEquals(nuevasCondiciones, resultado.getCondicionesContrato());
        verify(proveedorRepository).findById(PROVEEDOR_ID);
        verify(proveedorRepository).save(proveedor);
    }

    @Test
    void actualizarCondicionesContrato_ShouldThrowExceptionWhenProveedorNotFound() {
        // Arrange
        String nuevasCondiciones = "Nuevas condiciones de contrato";
        when(proveedorRepository.findById(PROVEEDOR_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            proveedorService.actualizarCondicionesContrato(PROVEEDOR_ID, nuevasCondiciones);
        });
        verify(proveedorRepository).findById(PROVEEDOR_ID);
        verify(proveedorRepository, never()).save(any());
    }
}