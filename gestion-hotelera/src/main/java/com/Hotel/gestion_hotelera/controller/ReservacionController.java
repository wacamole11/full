package com.Hotel.gestion_hotelera.controller;

import com.Hotel.gestion_hotelera.entity.Reservacion;
import com.Hotel.gestion_hotelera.service.ReservacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservaciones")
// Cambia 4200 por 3000
public class ReservacionController {

    @Autowired
    private ReservacionService reservacionService;

    /**
     * Endpoint para CREAR una nueva reservación.
     * Recibe los IDs de cliente y habitación como parámetros de la URL.
     * Recibe los detalles de la reservación (fechas, etc.) en el cuerpo de la petición.
     * HTTP Method: POST
     * URL: /api/reservaciones?clienteId=1&habitacionId=101
     */
    @PostMapping
    public ResponseEntity<?> createReservacion(@RequestBody Reservacion reservacion,
                                               @RequestParam Long clienteId,
                                               @RequestParam Long habitacionId) {
        try {
            Reservacion nuevaReservacion = reservacionService.crearReservacion(reservacion, clienteId, habitacionId);
            return new ResponseEntity<>(nuevaReservacion, HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Captura errores de validación de negocio (fechas, disponibilidad)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            // Captura errores de "no encontrado" (cliente, habitación)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Endpoint para OBTENER una reservación por su ID.
     * HTTP Method: GET
     * URL: /api/reservaciones/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reservacion> getReservacionById(@PathVariable Long id) {
        return reservacionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para OBTENER todas las reservaciones de un cliente.
     * HTTP Method: GET
     * URL: /api/reservaciones/cliente/1
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Reservacion>> getReservacionesByCliente(@PathVariable Long clienteId) {
        List<Reservacion> reservaciones = reservacionService.findByClienteId(clienteId);
        return ResponseEntity.ok(reservaciones);
    }

    /**
     * Endpoint para CANCELAR una reservación.
     * HTTP Method: PUT
     * URL: /api/reservaciones/1/cancelar
     */
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarReservacion(@PathVariable Long id) {
        try {
            return reservacionService.cancelarReservacion(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}