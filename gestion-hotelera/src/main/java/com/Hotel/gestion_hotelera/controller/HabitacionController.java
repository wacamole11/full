package com.Hotel.gestion_hotelera.controller;

import com.Hotel.gestion_hotelera.entity.Habitacion;
import com.Hotel.gestion_hotelera.service.HabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

// --- ANOTACIONES AÑADIDAS ---
@RestController
@RequestMapping("/api/habitaciones") // Define la ruta base para todos los endpoints de este controlador
// Cambia 4200 por 3000
public class HabitacionController {

    @Autowired
    private HabitacionService habitacionService;

    // GET: /api/habitaciones
    @GetMapping
    public List<Habitacion> getAllHabitaciones() {
        return habitacionService.findAll();
    }

    // GET: /api/habitaciones/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Habitacion> getHabitacionById(@PathVariable Long id) {
        return habitacionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET: /api/habitaciones/por-estado?estado=DISPONIBLE
    @GetMapping("/por-estado")
    public List<Habitacion> getHabitacionesByEstado(@RequestParam String estado) {
        try {
            // Convertimos el String a Enum para más flexibilidad
            Habitacion.EstadoHabitacion estadoEnum = Habitacion.EstadoHabitacion.valueOf(estado.toUpperCase());
            return habitacionService.findByEstado(estadoEnum);
        } catch (IllegalArgumentException e) {
            // Manejo por si envían un estado que no existe
            return List.of();
        }
    }

    // GET: /api/habitaciones/disponibles?entrada=2023-12-01T14:00:00&salida=2023-12-05T12:00:00
    @GetMapping("/disponibles")
    public ResponseEntity<List<Habitacion>> getHabitacionesDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime entrada,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime salida) {
        try {
            List<Habitacion> disponibles = habitacionService.findDisponibles(entrada, salida);
            return ResponseEntity.ok(disponibles);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Usar body para un futuro mensaje de error
        }
    }

    // POST: /api/habitaciones?tipoHabitacionId=1
    @PostMapping
    public ResponseEntity<Habitacion> createHabitacion(@RequestBody Habitacion habitacion, @RequestParam Long tipoHabitacionId) {
        try {
            Habitacion nuevaHabitacion = habitacionService.save(habitacion, tipoHabitacionId);
            return new ResponseEntity<>(nuevaHabitacion, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Devolver un mensaje de error más claro es una buena práctica
            return ResponseEntity.badRequest().body(null);
        }
    }

    // PUT: /api/habitaciones/{id}/estado?nuevoEstado=OCUPADA
    @PutMapping("/{id}/estado")
    public ResponseEntity<Habitacion> updateEstadoHabitacion(@PathVariable Long id, @RequestParam String nuevoEstado) {
        try {
            Habitacion.EstadoHabitacion estadoEnum = Habitacion.EstadoHabitacion.valueOf(nuevoEstado.toUpperCase());
            Habitacion habitacionActualizada = habitacionService.updateEstado(id, estadoEnum);
            return ResponseEntity.ok(habitacionActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Estado inválido
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Habitación no encontrada
        }
    }

    // DELETE: /api/habitaciones/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabitacion(@PathVariable Long id) {
        try {
            habitacionService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}