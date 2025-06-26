package com.Hotel.gestion_hotelera.repository;

import com.Hotel.gestion_hotelera.entity.Reservacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservacionRepository extends JpaRepository<Reservacion, Long> {

    // Buscar todas las reservaciones de un cliente específico
    List<Reservacion> findByClienteId(Long clienteId);

    // Buscar todas las reservaciones de una habitación específica
    List<Reservacion> findByHabitacionId(Long habitacionId);

    /**
     *  Encuentra reservaciones que se superponen con un rango de fechas dado para una habitación específica.
     *  Esto es CLAVE para comprobar la disponibilidad.
     *  Una superposición existe si (start1 < end2) y (end1 > start2).
     *  Excluimos las reservas ya canceladas.
     */
    @Query("SELECT r FROM Reservacion r WHERE r.habitacion.id = :habitacionId " +
            "AND r.estadoReserva <> 'CANCELADA' " +
            "AND r.fechaCheckin < :fechaCheckout " +
            "AND r.fechaCheckout > :fechaCheckin")
    List<Reservacion> findConflictingReservations(
            @Param("habitacionId") Long habitacionId,
            @Param("fechaCheckin") LocalDateTime fechaCheckin,
            @Param("fechaCheckout") LocalDateTime fechaCheckout);
}