package com.Hotel.gestion_hotelera.controller;

import com.Hotel.gestion_hotelera.entity.TipoHabitacion;
import com.Hotel.gestion_hotelera.service.TipoHabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/tipos-habitacion")
// Cambia 4200 por 3000
public class TipoHabitacionController {

    // Inyección de dependencias: Spring se encarga de crear una instancia de
    // TipoHabitacionService y "conectarla" aquí.
    @Autowired
    private TipoHabitacionService tipoHabitacionService;

    /**
     * Endpoint para OBTENER todos los tipos de habitación.
     * HTTP Method: GET
     * URL: /api/tipos-habitacion
     */
    @GetMapping
    public List<TipoHabitacion> getAllTiposHabitacion() {
        return tipoHabitacionService.findAll();
    }

    /**
     * Endpoint para OBTENER un solo tipo de habitación por su ID.
     * HTTP Method: GET
     * URL: /api/tipos-habitacion/1  (donde 1 es el ID)
     *
     * ResponseEntity permite tener más control sobre la respuesta HTTP,
     * como establecer el código de estado (ej. 200 OK, 404 Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoHabitacion> getTipoHabitacionById(@PathVariable Long id) {
        return tipoHabitacionService.findById(id)
                .map(tipo -> ResponseEntity.ok(tipo)) // Si se encuentra, devuelve 200 OK con el objeto
                .orElse(ResponseEntity.notFound().build()); // Si no, devuelve 404 Not Found
    }

    /**
     * Endpoint para CREAR un nuevo tipo de habitación.
     * HTTP Method: POST
     * URL: /api/tipos-habitacion
     * El cuerpo de la petición (@RequestBody) debe contener el JSON del nuevo tipo de habitación.
     */
    @PostMapping
    public ResponseEntity<TipoHabitacion> createTipoHabitacion(@RequestBody TipoHabitacion tipoHabitacion) {
        TipoHabitacion nuevoTipo = tipoHabitacionService.save(tipoHabitacion);
        // Devuelve el código 201 Created junto con el objeto recién creado.
        return new ResponseEntity<>(nuevoTipo, HttpStatus.CREATED);
    }

    /**
     * Endpoint para ACTUALIZAR un tipo de habitación existente.
     * HTTP Method: PUT
     * URL: /api/tipos-habitacion/1 (donde 1 es el ID del tipo a actualizar)
     * El cuerpo de la petición (@RequestBody) debe contener los nuevos datos.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoHabitacion> updateTipoHabitacion(@PathVariable Long id, @RequestBody TipoHabitacion tipoHabitacionDetails) {
        return tipoHabitacionService.update(id, tipoHabitacionDetails)
                .map(tipoActualizado -> ResponseEntity.ok(tipoActualizado)) // Si se actualiza, devuelve 200 OK
                .orElse(ResponseEntity.notFound().build()); // Si no se encuentra, devuelve 404
    }

    /**
     * Endpoint para ELIMINAR un tipo de habitación.
     * HTTP Method: DELETE
     * URL: /api/tipos-habitacion/1 (donde 1 es el ID del tipo a eliminar)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoHabitacion(@PathVariable Long id) {
        boolean eliminado = tipoHabitacionService.deleteById(id);
        if (eliminado) {
            // Devuelve el código 204 No Content, que es el estándar para un DELETE exitoso.
            return ResponseEntity.noContent().build();
        } else {
            // Si no se pudo eliminar (porque no existía), devuelve 404 Not Found.
            return ResponseEntity.notFound().build();
        }
    }
}