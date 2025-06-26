package com.Hotel.gestion_hotelera.service;

import com.Hotel.gestion_hotelera.entity.Habitacion;
import com.Hotel.gestion_hotelera.entity.TareaLimpieza;
import com.Hotel.gestion_hotelera.entity.Usuario;
import com.Hotel.gestion_hotelera.repository.HabitacionRepository;
import com.Hotel.gestion_hotelera.repository.TareaLimpiezaRepository;
import com.Hotel.gestion_hotelera.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TareaLimpiezaService {

    @Autowired
    private TareaLimpiezaRepository tareaLimpiezaRepository;
    @Autowired
    private HabitacionRepository habitacionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public TareaLimpieza asignarTarea(Long habitacionId, Long personalId, String notas) {
        Habitacion habitacion = habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada con ID: " + habitacionId));

        Usuario personal = usuarioRepository.findById(personalId)
                .orElseThrow(() -> new RuntimeException("Personal no encontrado con ID: " + personalId));

        // Opcional: Validar que el usuario tenga el rol 'LIMPIEZA'
        // if (personal.getRol() != Usuario.Rol.LIMPIEZA) { ... }

        // Lógica de negocio: al asignar una tarea, la habitación debe marcarse como sucia o en limpieza.
        habitacion.setEstado(Habitacion.EstadoHabitacion.SUCIA);
        habitacionRepository.save(habitacion);

        TareaLimpieza nuevaTarea = new TareaLimpieza();
        nuevaTarea.setHabitacion(habitacion);
        nuevaTarea.setPersonalAsignado(personal);
        nuevaTarea.setEstado(TareaLimpieza.EstadoLimpieza.PENDIENTE);
        nuevaTarea.setNotas(notas);

        return tareaLimpiezaRepository.save(nuevaTarea);
    }

    @Transactional
    public Optional<TareaLimpieza> iniciarLimpieza(Long tareaId) {
        return tareaLimpiezaRepository.findById(tareaId).map(tarea -> {
            tarea.setEstado(TareaLimpieza.EstadoLimpieza.EN_PROCESO);
            tarea.setFechaInicio(LocalDateTime.now());

            // Actualizar estado de la habitación
            Habitacion habitacion = tarea.getHabitacion();
            habitacion.setEstado(Habitacion.EstadoHabitacion.EN_LIMPIEZA);
            habitacionRepository.save(habitacion);

            return tareaLimpiezaRepository.save(tarea);
        });
    }

    @Transactional
    public Optional<TareaLimpieza> finalizarLimpieza(Long tareaId) {
        return tareaLimpiezaRepository.findById(tareaId).map(tarea -> {
            tarea.setEstado(TareaLimpieza.EstadoLimpieza.COMPLETADA);
            tarea.setFechaFin(LocalDateTime.now());

            // ¡Paso clave! La habitación ahora está disponible para el siguiente huésped.
            Habitacion habitacion = tarea.getHabitacion();
            habitacion.setEstado(Habitacion.EstadoHabitacion.DISPONIBLE);
            habitacionRepository.save(habitacion);

            return tareaLimpiezaRepository.save(tarea);
        });
    }

    @Transactional
    public Optional<TareaLimpieza> reportarIncidencia(Long tareaId, String notasIncidencia) {
        return tareaLimpiezaRepository.findById(tareaId).map(tarea -> {
            tarea.setEstado(TareaLimpieza.EstadoLimpieza.CON_INCIDENCIA);
            String notasActuales = tarea.getNotas() != null ? tarea.getNotas() + "\n" : "";
            tarea.setNotas(notasActuales + "INCIDENCIA: " + notasIncidencia);

            // Una incidencia usualmente pone la habitación en mantenimiento.
            Habitacion habitacion = tarea.getHabitacion();
            habitacion.setEstado(Habitacion.EstadoHabitacion.MANTENIMIENTO);
            habitacionRepository.save(habitacion);

            return tareaLimpiezaRepository.save(tarea);
        });
    }

    public List<TareaLimpieza> findByPersonalAsignadoId(Long personalId) {
        return tareaLimpiezaRepository.findByPersonalAsignadoId(personalId);
    }
}