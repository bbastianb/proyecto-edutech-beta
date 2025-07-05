package com.proyecto.Edutech_v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.proyecto.Edutech_v1.model.Curso;
import com.proyecto.Edutech_v1.model.Estudiante;
import com.proyecto.Edutech_v1.model.Gerente;
import com.proyecto.Edutech_v1.model.Incidencia;
import com.proyecto.Edutech_v1.model.Instructor;
import com.proyecto.Edutech_v1.model.LogicaSoporte;
import com.proyecto.Edutech_v1.model.Proveedor;
import com.proyecto.Edutech_v1.repository.CursoRepository;
import com.proyecto.Edutech_v1.repository.EstudianteRepository;
import com.proyecto.Edutech_v1.repository.GerenteRepository;
import com.proyecto.Edutech_v1.repository.IncidenciaRepository;
import com.proyecto.Edutech_v1.repository.InstructorRepository;
import com.proyecto.Edutech_v1.repository.LogicaSoporteRepository;
import com.proyecto.Edutech_v1.repository.ProveedorRepository;

import net.datafaker.Faker;

@Component
@Profile("dev") // Entrar con ese perfil para que funcione esta clase!!
public class DataLoader implements CommandLineRunner {
    // CommandLineRunner Corre este codigo automaticamante "Sin la nececidad que dar
    // accion a a un metodo,ect"

    @Autowired
    private GerenteRepository gerenteRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private LogicaSoporteRepository logicaSoporteRepository;

    @Autowired
    private IncidenciaRepository incidenciaRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public void run(String... args) throws Exception {

        // Crea eL "Feka" y "Random" utilizando las bilbiotecas
        Faker faker = new Faker();
        Random random = new Random();

        // Metodo para crear Gerentes Aleatorios
        for (int i = 0; i < 5; i++) {
            // Recorre el bucle 5 veces Crea y Setea datos al Gerente
            Gerente gerente = new Gerente();
            gerente.setNombreGere(faker.name().firstName());
            gerente.setApellidoGere(faker.name().lastName());
            gerente.setEmailGere(faker.internet().emailAddress());
            gerente.setContraseñaGere(faker.internet().password(8, 16));
            gerente.setTelefonoGere(faker.phoneNumber().cellPhone());
            gerente.setFechaRegistro(faker.date().past(1000, java.util.concurrent.TimeUnit.DAYS));
            gerente.setEspecialidad(faker.job().field());
            gerente.setCursosGestionados((double) faker.number().numberBetween(1, 15));
            gerenteRepository.save(gerente);
        }
        // Recorre la lista Gerente y va creando Gerentes para poder asignarlo a los
        // cursos
        List<Gerente> gerentes = gerenteRepository.findAll();

        // Metodo para crear Cursos
        List<Curso> cursosGenerados = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            // Ejecuta el blucle 20 Veces, Crea Y Setea datos al Curso
            Curso curso = new Curso();
            curso.setCodigoCurso("CUR-" + faker.code().asin());
            curso.setTituloCurso(faker.educator().course());
            curso.setDescripcionCurso(faker.lorem().sentence(10));
            curso.setCategoriaCurso(faker.book().genre());
            curso.setPrecioCurso(20000 + random.nextInt(180001));
            curso.setDuracionHorasCurso(faker.number().numberBetween(10, 100));
            curso.setCertificacionDisponibleCurso(faker.bool().bool());
            curso.setResenaCurso(faker.lorem().paragraph(1));
            cursoRepository.save(curso);
            cursosGenerados.add(curso);
        }

        // Generar estudiante
        java.util.List<Curso> cursos = new java.util.ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Estudiante estudiante = new Estudiante(
                    null,
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.internet().emailAddress(),
                    faker.internet().password(8, 100),
                    faker.phoneNumber().cellPhone(),
                    faker.date().past(1000, java.util.concurrent.TimeUnit.DAYS),
                    faker.business().creditCardType(),
                    random.nextInt(5) + 1,
                    faker.number().randomDouble(0, 0, 2000000),null
            );
            estudianteRepository.save(estudiante);
        }

        // Generar instructor
        String[] especialidadesOnline = {
                "Desarrollo Web",
                "Data Science",
                "Inteligencia Artificial",
                "Ciberseguridad",
                "Marketing Digital",
                "Diseño UX/UI",
                "Gestión de Proyectos",
                "Finanzas Personales",
                "Idiomas",
                "Ofimática",
                "Desarrollo de Apps Móviles",
                "Machine Learning",
                "Redes y Sistemas",
                "Cloud Computing",
                "Emprendimiento"
        };

        for (int i = 0; i < 8; i++) {
            int cursosImpartidos = random.nextInt(10) + 1;
            StringBuilder nombresCursos = new StringBuilder();

            for (int j = 0; j < cursosImpartidos; j++) {
                if (j > 0)
                    nombresCursos.append(", ");
                nombresCursos.append(faker.book().title());
            }

            List<Curso> cursosAsignadosInstructor = new ArrayList<>();
            int cantidad = random.nextInt(cursosGenerados.size()) + 1;
            Collections.shuffle(cursosGenerados);
            for (int j = 0; j < cantidad; j++) {
                cursosAsignadosInstructor.add(cursosGenerados.get(j));
            }

            Instructor instructor = new Instructor(
                    null,
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.internet().emailAddress(),
                    faker.internet().password(8, 16),
                    faker.phoneNumber().cellPhone(),
                    faker.date().past(1500, java.util.concurrent.TimeUnit.DAYS),
                    especialidadesOnline[random.nextInt(especialidadesOnline.length)], // especialidad realista
                    cursosImpartidos,
                    nombresCursos.toString(),
                    faker.bool().bool(),
                    cursosAsignadosInstructor);
            instructorRepository.save(instructor);
        }

        // Generar LogicaSoporte
        String[] turnos = { "Mañana", "Tarde", "Noche" };
        String[] herramientas = { "Zendesk, Jira", "Freshdesk, Slack", "ServiceNow, Teams", "Intercom, Zoom" };

        for (int i = 0; i < 5; i++) {
            LogicaSoporte soporte = new LogicaSoporte(
                    null,
                    turnos[random.nextInt(turnos.length)],
                    random.nextInt(100) + 1, // incidentes resueltos entre 1 y 100
                    herramientas[random.nextInt(herramientas.length)]);
            logicaSoporteRepository.save(soporte);
        }

        // Generar Proveedor
        for (int i = 0; i < 5; i++) {
            Proveedor proveedor = new Proveedor(
                    null,
                    faker.company().name(), // nombre
                    faker.company().industry(), // servicio
                    faker.phoneNumber().phoneNumber(), // contacto
                    faker.address().fullAddress(), // direccion
                    random.nextInt(48) + 1, // tiempoRespuestaHoras entre 1 y 48
                    faker.lorem().sentence(10) // condicionesContrato
            );
            proveedorRepository.save(proveedor);
        }

        

        // Generar Incidencia
        List<LogicaSoporte> soportes = new ArrayList<>();
        logicaSoporteRepository.findAll().forEach(soportes::add);

        List<Instructor> instructores = new ArrayList<>();
        instructorRepository.findAll().forEach(instructores::add);

        String[] prioridades = { "Alta", "Media", "Baja" };
        String[] estados = { "Abierta", "En Progreso", "Resuelta", "Cerrada" };

        for (int i = 0; i < 10; i++) {
            Date fechaReporte = faker.date().past(60, java.util.concurrent.TimeUnit.DAYS);
            Date fechaResolucion = faker.bool().bool()
                    ? faker.date().future(30, java.util.concurrent.TimeUnit.DAYS, fechaReporte)
                    : null;

            Incidencia incidencia = new Incidencia(
                    null,
                    faker.lorem().sentence(3), // titulo
                    faker.lorem().sentence(10), // descripcion
                    prioridades[random.nextInt(prioridades.length)],
                    estados[random.nextInt(estados.length)],
                    fechaReporte,
                    fechaResolucion,
                    soportes.isEmpty() ? null : soportes.get(random.nextInt(soportes.size())),
                    instructores.isEmpty() ? null : instructores.get(random.nextInt(instructores.size())));
            incidenciaRepository.save(incidencia);
        }
    }
}
