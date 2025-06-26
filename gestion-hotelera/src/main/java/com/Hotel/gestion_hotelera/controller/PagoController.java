package com.Hotel.gestion_hotelera.controller;

import com.Hotel.gestion_hotelera.entity.Pago;
import com.Hotel.gestion_hotelera.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // Ruta base para todo el controlador
// Cambia 4200 por 3000
public class PagoController {

    @Autowired
    private PagoService pagoService;

    /**
     * Endpoint para OBTENER todos los pagos de una reservación específica.
     * Es más RESTful anidar el recurso "pagos" bajo su recurso padre "reservaciones".
     * HTTP Method: GET
     * URL: /api/reservaciones/1/pagos
     */
    @GetMapping("/reservaciones/{reservacionId}/pagos")
    public ResponseEntity<List<Pago>> getPagosByReservacionId(@PathVariable Long reservacionId) {
        List<Pago> pagos = pagoService.findPagosByReservacionId(reservacionId);
        return ResponseEntity.ok(pagos);
    }

    /**
     * Endpoint para OBTENER un pago específico por su ID.
     * HTTP Method: GET
     * URL: /api/pagos/1
     */
    @GetMapping("/pagos/{id}")
    public ResponseEntity<Pago> getPagoById(@PathVariable Long id) {
        return pagoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para CREAR un nuevo pago para una reservación.
     * HTTP Method: POST
     * URL: /api/reservaciones/1/pagos
     * El cuerpo de la petición contiene el monto y el método de pago.
     */
    @PostMapping("/reservaciones/{reservacionId}/pagos")
    public ResponseEntity<Pago> createPago(@PathVariable Long reservacionId, @RequestBody Pago pago) {
        try {
            Pago nuevoPago = pagoService.createPago(reservacionId, pago);
            return new ResponseEntity<>(nuevoPago, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build(); // O devolver un cuerpo con el mensaje de error
        }
    }

    /**
     * Endpoint para ACTUALIZAR el estado de un pago.
     * HTTP Method: PUT
     * URL: /api/pagos/1/estado?nuevoEstado=COMPLETADO
     */
    @PutMapping("/pagos/{pagoId}/estado")
    public ResponseEntity<Pago> updateEstadoPago(@PathVariable Long pagoId, @RequestParam String nuevoEstado) {
        try {
            Pago.EstadoPago estadoEnum = Pago.EstadoPago.valueOf(nuevoEstado.toUpperCase());
            return pagoService.updateEstadoPago(pagoId, estadoEnum)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Si el estado no es válido
        }
    }
}