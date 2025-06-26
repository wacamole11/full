package com.Hotel.gestion_hotelera.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tareas_limpieza")
@Data
@NoArgsConstructor      // <-- AÑADIR ESTA LÍNEA
@AllArgsConstructor     // <-- AÑADIR ESTA LÍNEA
public class TareaLimpieza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EstadoLimpieza estado;

    @CreationTimestamp // Se establece automáticamente al crear la tarea
    @Column(name = "fecha_asignacion", updatable = false)
    private LocalDateTime fechaAsignacion;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(columnDefinition = "TEXT")
    private String notas;

    // --- RELACIONES ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitacion_id", nullable = false)
    @JsonBackReference("habitacion-limpieza") // Evita recursión con Habitacion
    private Habitacion habitacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_id", nullable = false)
    @JsonBackReference("usuario-limpieza") // Evita recursión con Usuario
    private Usuario personalAsignado;


    // --- ENUM para el estado de la limpieza ---
    public enum EstadoLimpieza {
        PENDIENTE,
        EN_PROCESO,
        COMPLETADA,
        CON_INCIDENCIA // Para reportar problemas en la habitación
    }
}