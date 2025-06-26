package com.Hotel.gestion_hotelera.controller;

import com.Hotel.gestion_hotelera.entity.TareaLimpieza;
import com.Hotel.gestion_hotelera.service.TareaLimpiezaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/limpieza/tareas")
// Cambia 4200 por 3000
public class TareaLimpiezaController {

    @Autowired
    private TareaLimpiezaService tareaLimpiezaService;

    /**
     * Endpoint para ASIGNAR una nueva tarea de limpieza (rol Admin).
     * HTTP Method: POST
     * URL: /api/limpieza/tareas
     */
    @PostMapping
    public ResponseEntity<?> asignarTarea(@RequestParam Long habitacionId,
                                          @RequestParam Long personalId,
                                          @RequestBody(required = false) Map<String, String> body) {
        try {
            String notas = (body != null) ? body.get("notas") : "";
            TareaLimpieza nuevaTarea = tareaLimpiezaService.asignarTarea(habitacionId, personalId, notas);
            return new ResponseEntity<>(nuevaTarea, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint para que el personal de limpieza VEA sus tareas asignadas.
     * HTTP Method: GET
     * URL: /api/limpieza/tareas/personal/1
     */
    @GetMapping("/personal/{personalId}")
    public ResponseEntity<List<TareaLimpieza>> getTareasPorPersonal(@PathVariable Long personalId) {
        List<TareaLimpieza> tareas = tareaLimpiezaService.findByPersonalAsignadoId(personalId);
        return ResponseEntity.ok(tareas);
    }

    /**
     * Endpoint para INICIAR una tarea de limpieza (rol Limpieza).
     * HTTP Method: PUT
     * URL: /api/limpieza/tareas/1/iniciar
     */
    @PutMapping("/{tareaId}/iniciar")
    public ResponseEntity<TareaLimpieza> iniciarLimpieza(@PathVariable Long tareaId) {
        return tareaLimpiezaService.iniciarLimpieza(tareaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para FINALIZAR una tarea de limpieza (rol Limpieza).
     * HTTP Method: PUT
     * URL: /api/limpieza/tareas/1/finalizar
     */
    @PutMapping("/{tareaId}/finalizar")
    public ResponseEntity<TareaLimpieza> finalizarLimpieza(@PathVariable Long tareaId) {
        return tareaLimpiezaService.finalizarLimpieza(tareaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para REPORTAR una incidencia en una tarea (rol Limpieza).
     * HTTP Method: PUT
     * URL: /api/limpieza/tareas/1/incidencia
     */
    @PutMapping("/{tareaId}/incidencia")
    public ResponseEntity<TareaLimpieza> reportarIncidencia(@PathVariable Long tareaId, @RequestBody Map<String, String> body) {
        String notasIncidencia = body.get("notas");
        if (notasIncidencia == null || notasIncidencia.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return tareaLimpiezaService.reportarIncidencia(tareaId, notasIncidencia)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}