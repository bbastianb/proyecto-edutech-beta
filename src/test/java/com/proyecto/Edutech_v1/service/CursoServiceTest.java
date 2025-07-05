package com.proyecto.Edutech_v1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.proyecto.Edutech_v1.model.Curso;
import com.proyecto.Edutech_v1.model.Estudiante;
import com.proyecto.Edutech_v1.model.Gerente;
import com.proyecto.Edutech_v1.model.Instructor;
import com.proyecto.Edutech_v1.repository.CursoRepository;
import com.proyecto.Edutech_v1.repository.EstudianteRepository;
import com.proyecto.Edutech_v1.repository.GerenteRepository;
import com.proyecto.Edutech_v1.repository.InstructorRepository;

//@SpringBootTest <-- esto para test activo es mas real pero se necesita la bbdd <--- activa el contexto
public class CursoServiceTest {

    //@Autowired esto es de SpringBootTest como que autoriza para que el administre los objetos entonces sin el queda null
    @InjectMocks
    private CursoService cursoService;

    //@MockBean <-- Test Activo simula el comportamiento real de la bbdd
    //private CursoRepository cursoRepository;
    
    @Mock  //<--- Simula los Objetos falsos
    private CursoRepository cursoRepository;

    //@MockBean <-- Test Activos
    //private EstudianteRepository estudianteRepository;

    @Mock
    private EstudianteRepository estudianteRepository;

    //@MockBean <-- Test Activos
    //private InstructorRepository instructorRepository;

    @Mock
    private InstructorRepository instructorRepository;

    //@MockBean <-- Test Activos
    //private GerenteRepository gerenteRepository;

    @Mock
    private GerenteRepository gerenteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // <-- para pruebas sin SpringBootTest
        Mockito.reset(cursoRepository); // Reinicia el Mock de cursoRepository
    }


    // Test metodo ObtenerCursos
    @Test
    void testObtenerCursos() {
        List<Curso> cursos = cursoService.obtenerCursos();
        assertTrue(cursos.isEmpty(), "No hay cursos cargados");
    }

    // Test metodo BuscarPorId
    @Test
    void testBuscarPorId() {

        Curso curso = new Curso();
        curso.setCodigoCurso("CURS001");

        when(cursoRepository.findById("CURS001")).thenReturn(Optional.of(curso));

        Optional<Curso> resultado = cursoService.buscarPorId("CURS001");

        assertTrue(resultado.isPresent());
        assertEquals("CURS001", resultado.get().getCodigoCurso());

    }

    @Test
    public void testBuscarPorId_NoEncontrado() {
        when(cursoRepository.findById("NO_EXISTE")).thenReturn(Optional.empty());

        Optional<Curso> resultado = cursoService.buscarPorId("NO_EXISTE");

        assertFalse(resultado.isPresent());
    }

    @Test
    public void testGuardarCurso() {
        Curso curso = new Curso();
        curso.setCodigoCurso("CURS001");

        when(cursoRepository.save(curso)).thenReturn(curso);

        Curso resultado = cursoService.guardarCurso(curso);

        assertNotNull(resultado);
        assertEquals("CURS001", resultado.getCodigoCurso());
    }


    @Test
    public void testInscribirEstudiantePorId_EstudianteNoExiste() {
        Curso curso = new Curso();

        when(estudianteRepository.findById(99L)).thenReturn(Optional.empty());

        boolean resultado = cursoService.inscribirEstudiantePorId(curso, 99L);

        assertFalse(resultado);
        verify(estudianteRepository, never()).save(any());
    }



    @Test
    public void testInscribirEstudiantePorId_YaInscrito() {
        Curso curso = new Curso();
        curso.setEstudiantesInscritos(new ArrayList<>());

        Estudiante estudiante = new Estudiante();
        estudiante.setIdEstudiante(1L);
        estudiante.setCurso(curso);
        curso.getEstudiantesInscritos().add(estudiante);

        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));

        boolean resultado = cursoService.inscribirEstudiantePorId(curso, 1L);

        assertFalse(resultado);
        verify(estudianteRepository, never()).save(any());
    }



    @Test
    public void testInscribirEstudiantePorId_Exitoso() {
        Curso curso = new Curso();
        curso.setEstudiantesInscritos(new ArrayList<>());

        Estudiante estudiante = new Estudiante();
        estudiante.setIdEstudiante(1L);
        estudiante.setCurso(null);

        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));

        boolean resultado = cursoService.inscribirEstudiantePorId(curso, 1L);

        assertTrue(resultado);
        assertEquals(curso, estudiante.getCurso());
        assertTrue(curso.getEstudiantesInscritos().contains(estudiante));
        verify(estudianteRepository).save(estudiante);
    }



    @Test
    public void testAsignarGerentePorId_Exitoso() {
        Curso curso = new Curso();
        Gerente gerente = new Gerente();
        gerente.setIdGerente(1L);

        when(gerenteRepository.findById(1L)).thenReturn(Optional.of(gerente));
        when(cursoRepository.save(curso)).thenReturn(curso);

        boolean resultado = cursoService.asignarGerentePorId(curso, 1L);

        assertTrue(resultado);
        assertEquals(gerente, curso.getGerente());
        verify(cursoRepository).save(curso);
    }



    @Test
    public void testAsignarGerentePorId_NoExisteGerente() {
        Curso curso = new Curso();

        when(gerenteRepository.findById(1L)).thenReturn(Optional.empty());

        boolean resultado = cursoService.asignarGerentePorId(curso, 1L);

        assertFalse(resultado);
        verify(cursoRepository, never()).save(any());
    }



    @Test
    public void testAsignarGerentePorId_YaAsignado() {
        Curso curso = new Curso();
        Gerente gerente = new Gerente();
        gerente.setIdGerente(1L);
        curso.setGerente(gerente);

        boolean resultado = cursoService.asignarGerentePorId(curso, 1L);

        assertFalse(resultado);
        verify(cursoRepository, never()).save(any());
    }



    @Test
    public void testAsignarInstructorPorId_Exitoso() {
        Curso curso = new Curso();
        Instructor instructor = new Instructor();
        instructor.setIdIntructor(1L);

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(cursoRepository.save(curso)).thenReturn(curso);

        boolean resultado = cursoService.asignarInstructorPorId(curso, 1L);

        assertTrue(resultado);
        assertEquals(instructor, curso.getInstructor());
        verify(cursoRepository).save(curso);
    }



    @Test
    public void testAsignarInstructorPorId_NoExisteInstructor() {
        Curso curso = new Curso();

        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        boolean resultado = cursoService.asignarInstructorPorId(curso, 1L);

        assertFalse(resultado);
        verify(cursoRepository, never()).save(any());
    }



    @Test
    public void testAsignarInstructorPorId_YaAsignado() {
        Curso curso = new Curso();
        Instructor instructor = new Instructor();
        instructor.setIdIntructor(1L);
        curso.setInstructor(instructor);

        boolean resultado = cursoService.asignarInstructorPorId(curso, 1L);

        assertFalse(resultado);
        verify(cursoRepository, never()).save(any());
    }

}
