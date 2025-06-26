package com.Hotel.gestion_hotelera.service;

import com.Hotel.gestion_hotelera.entity.Habitacion;
import com.Hotel.gestion_hotelera.entity.Reservacion;
import com.Hotel.gestion_hotelera.entity.Usuario;
import com.Hotel.gestion_hotelera.repository.HabitacionRepository;
import com.Hotel.gestion_hotelera.repository.ReservacionRepository;
import com.Hotel.gestion_hotelera.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservacionService {

    @Autowired
    private ReservacionRepository reservacionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private HabitacionRepository habitacionRepository;

    @Transactional
    public Reservacion crearReservacion(Reservacion reservacion, Long clienteId, Long habitacionId) {
        // 1. Validar que la fecha de check-in sea anterior a la de check-out
        if (!reservacion.getFechaCheckin().isBefore(reservacion.getFechaCheckout())) {
            throw new IllegalArgumentException("La fecha de check-in debe ser anterior a la fecha de check-out.");
        }

        // 2. Verificar disponibilidad de la habitación
        List<Reservacion> conflictos = reservacionRepository.findConflictingReservations(
                habitacionId, reservacion.getFechaCheckin(), reservacion.getFechaCheckout());
        if (!conflictos.isEmpty()) {
            throw new IllegalStateException("La habitación no está disponible en las fechas seleccionadas.");
        }

        // 3. Obtener las entidades Cliente y Habitación
        Usuario cliente = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        Habitacion habitacion = habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada con ID: " + habitacionId));

        // 4. Calcular el monto total
        long noches = Duration.between(reservacion.getFechaCheckin(), reservacion.getFechaCheckout()).toDays();
        if (noches == 0) noches = 1; // Mínimo una noche de cobro
        BigDecimal montoTotal = habitacion.getPrecioPorNoche().multiply(new BigDecimal(noches));
        reservacion.setMontoTotal(montoTotal);

        // 5. Asignar entidades y estado inicial
        reservacion.setCliente(cliente);
        reservacion.setHabitacion(habitacion);
        reservacion.setEstadoReserva(Reservacion.EstadoReserva.PENDIENTE); // Estado inicial

        return reservacionRepository.save(reservacion);
    }

    @Transactional
    public Optional<Reservacion> cancelarReservacion(Long id) {
        return reservacionRepository.findById(id).map(reservacion -> {
            // Lógica de negocio: No se puede cancelar si ya está checked-in o checked-out
            if (reservacion.getEstadoReserva() == Reservacion.EstadoReserva.CHECKED_IN ||
                    reservacion.getEstadoReserva() == Reservacion.EstadoReserva.CHECKED_OUT) {
                throw new IllegalStateException("No se puede cancelar una reservación con check-in o check-out realizado.");
            }
            reservacion.setEstadoReserva(Reservacion.EstadoReserva.CANCELADA);
            // Aquí se podría integrar lógica para reembolsar pagos
            return reservacionRepository.save(reservacion);
        });
    }

    @Transactional(readOnly = true)
    public Optional<Reservacion> findById(Long id) {
        return reservacionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Reservacion> findByClienteId(Long clienteId) {
        return reservacionRepository.findByClienteId(clienteId);
    }
}