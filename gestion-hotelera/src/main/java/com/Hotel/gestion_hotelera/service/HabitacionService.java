package com.Hotel.gestion_hotelera.service;

import com.Hotel.gestion_hotelera.entity.Habitacion;
import com.Hotel.gestion_hotelera.entity.TipoHabitacion;
import com.Hotel.gestion_hotelera.repository.HabitacionRepository;
import com.Hotel.gestion_hotelera.repository.TipoHabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // --- ANOTACIÓN AÑADIDA ---
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// --- ANOTACIÓN AÑADIDA ---
@Service
public class HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private TipoHabitacionRepository tipoHabitacionRepository;

    @Transactional(readOnly = true) // Buena práctica para métodos de solo lectura
    public List<Habitacion> findAll() {
        return habitacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Habitacion> findById(Long id) {
        return habitacionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Habitacion> findByEstado(Habitacion.EstadoHabitacion estado) {
        return habitacionRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<Habitacion> findDisponibles(LocalDateTime fechaEntrada, LocalDateTime fechaSalida) {
        if(fechaEntrada.isAfter(fechaSalida) || fechaEntrada.isEqual(fechaSalida)) {
            throw new IllegalArgumentException("La fecha de entrada debe ser anterior a la fecha de salida.");
        }
        // La consulta JPQL necesita los parámetros con nombres
        return habitacionRepository.findHabitacionesDisponibles(fechaEntrada, fechaSalida);
    }

    @Transactional // Para operaciones que modifican la base de datos
    public Habitacion save(Habitacion habitacion, Long tipoHabitacionId) {
        // Validación y asignación más eficientes
        TipoHabitacion tipo = tipoHabitacionRepository.findById(tipoHabitacionId)
                .orElseThrow(() -> new RuntimeException("Tipo de habitación no encontrado con id: " + tipoHabitacionId));

        habitacion.setTipoHabitacion(tipo);
        // Puedes agregar más lógica de negocio aquí, como validar que el número de habitación no exista

        return habitacionRepository.save(habitacion);
    }

    @Transactional
    public Habitacion updateEstado(Long id, Habitacion.EstadoHabitacion nuevoEstado) {
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada con id: " + id));
        habitacion.setEstado(nuevoEstado);
        return habitacionRepository.save(habitacion);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!habitacionRepository.existsById(id)) {
            throw new RuntimeException("Habitación no encontrada con id: " + id);
        }
        habitacionRepository.deleteById(id);
    }
}