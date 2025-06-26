package com.Hotel.gestion_hotelera.repository;

import com.Hotel.gestion_hotelera.entity.TipoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoHabitacionRepository extends JpaRepository<TipoHabitacion, Long> {
    // Spring Data JPA provee los métodos básicos (findById, findAll, save, deleteById, etc.)
    // Puedes añadir métodos personalizados si los necesitas, ej: Optional<TipoHabitacion> findByNombre(String nombre);
}