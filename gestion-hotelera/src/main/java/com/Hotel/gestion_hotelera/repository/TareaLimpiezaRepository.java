package com.Hotel.gestion_hotelera.repository;

import com.Hotel.gestion_hotelera.entity.TareaLimpieza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaLimpiezaRepository extends JpaRepository<TareaLimpieza, Long> {

    // Encuentra todas las tareas asignadas a un miembro del personal
    List<TareaLimpieza> findByPersonalAsignadoId(Long personalId);

    // Encuentra todas las tareas con un estado específico (ej. todas las 'PENDIENTE')
    List<TareaLimpieza> findByEstado(TareaLimpieza.EstadoLimpieza estado);

    // Encuentra tareas para un miembro del personal con un estado específico
    List<TareaLimpieza> findByPersonalAsignadoIdAndEstado(Long personalId, TareaLimpieza.EstadoLimpieza estado);
}