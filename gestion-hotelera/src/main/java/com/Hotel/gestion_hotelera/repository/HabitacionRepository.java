package com.Hotel.gestion_hotelera.repository;

import com.Hotel.gestion_hotelera.entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // --- IMPORTACIÓN AÑADIDA ---
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    List<Habitacion> findByEstado(Habitacion.EstadoHabitacion estado);

    // CORRECCIÓN: Tu consulta en el archivo original asume que en Reservacion las fechas se llaman 'fechaCheckin'
    // y 'fechaCheckout'. Asegúrate de que esos nombres coincidan cuando crees la entidad Reservacion.
    // También añadimos @Param para enlazar las variables.
    @Query("SELECT h FROM Habitacion h WHERE h.id NOT IN (" +
            "SELECT r.habitacion.id FROM Reservacion r WHERE " +
            "(r.fechaCheckin < :fechaSalida AND r.fechaCheckout > :fechaEntrada) " +
            "AND r.estadoReserva <> 'CANCELADA')")
    List<Habitacion> findHabitacionesDisponibles(@Param("fechaEntrada") LocalDateTime fechaEntrada, @Param("fechaSalida") LocalDateTime fechaSalida);
}