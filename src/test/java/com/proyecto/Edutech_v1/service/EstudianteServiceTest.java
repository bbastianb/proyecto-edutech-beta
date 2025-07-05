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
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.proyecto.Edutech_v1.model.Estudiante;
import com.proyecto.Edutech_v1.repository.EstudianteRepository;

public class EstudianteServiceTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @InjectMocks
    private EstudianteService estudianteService;

    private Estudiante estudiante1;
    private Estudiante estudiante2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        estudiante1 = new Estudiante();
        estudiante1.setIdEstudiante(1L);
        estudiante1.setNombre("Bastian");
        estudiante1.setApellido("Burgos");
        estudiante1.setEmail("burgosbastian@gmail.com");
        estudiante1.setContraseña("123");
        estudiante1.setTelefono("+56912345678");
        estudiante1.setMetodoPago("Debito");
        estudiante1.setCursoIncrito(1);
        estudiante1.setSaldoDisponible(100000);
        estudiante1.setFechaRegistro(new Date());

        estudiante2 = new Estudiante();
        estudiante2.setIdEstudiante(2L);
        estudiante2.setNombre("Pedro");
        estudiante2.setApellido("Perez");
        estudiante2.setEmail("pedro@gmail.com");
        estudiante2.setContraseña("456");
        estudiante2.setTelefono("+56987654321");
        estudiante2.setMetodoPago("Mercado Pago");
        estudiante2.setCursoIncrito(2);
        estudiante2.setSaldoDisponible(80000);
        estudiante2.setFechaRegistro(new Date());
    }

    @Test
    public void testObtenerEstudiantes() {
        when(estudianteRepository.findAll()).thenReturn(Arrays.asList(estudiante1, estudiante2));

        List<Estudiante> resultado = estudianteService.obtenerEstudiantes();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(estudianteRepository, times(1)).findAll();
    }

    @Test
    public void testGuardarEstudiante() {
        when(estudianteRepository.save(estudiante1)).thenReturn(estudiante1);

        Estudiante resultado = estudianteService.guardarEstudiante(estudiante1);

        assertNotNull(resultado);
        assertEquals("Bastian", resultado.getNombre());
        verify(estudianteRepository, times(1)).save(estudiante1);
    }

    @Test
    public void testActualizarEstudiante_CuandoExiste() {
        Estudiante datosActualizados = new Estudiante();
        datosActualizados.setNombre("Bastian Actualizado");
        datosActualizados.setApellido("Nuevo Apellido");
        datosActualizados.setEmail("nuevo@email.com");
        datosActualizados.setContraseña("nueva123");
        datosActualizados.setTelefono("+56999999999");
        datosActualizados.setMetodoPago("Crédito");
        datosActualizados.setCursoIncrito(3);
        datosActualizados.setSaldoDisponible(50000);

        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante1));
        when(estudianteRepository.save(any(Estudiante.class))).thenReturn(estudiante1);

        Estudiante resultado = estudianteService.actualizarEstudiante(1L, datosActualizados);

        assertNotNull(resultado);
        assertEquals("Bastian Actualizado", resultado.getNombre());
        assertEquals("Nuevo Apellido", resultado.getApellido());
        verify(estudianteRepository, times(1)).findById(1L);
        verify(estudianteRepository, times(1)).save(estudiante1);
    }

    @Test
    public void testActualizarEstudiante_CuandoNoExiste() {
        when(estudianteRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            estudianteService.actualizarEstudiante(99L, estudiante1);
        });

        assertEquals("Estudiante no encontrado", excepcion.getMessage());
        verify(estudianteRepository, times(1)).findById(99L);
        verify(estudianteRepository, never()).save(any());
    }

    @Test
    public void testEliminarEstudiante() {
        doNothing().when(estudianteRepository).deleteById(1L);

        estudianteService.eliminarEstudiante(1L);

        verify(estudianteRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testObtenerEstudiantesConInstructorYCursos() {
        when(estudianteRepository.findAll()).thenReturn(Arrays.asList(estudiante1, estudiante2));

        List<Estudiante> resultado = estudianteService.obtenerEstudiantesConInstructorYCursos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(estudianteRepository, times(1)).findAll();
    }
}
