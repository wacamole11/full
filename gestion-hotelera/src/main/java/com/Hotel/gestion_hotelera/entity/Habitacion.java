package com.Hotel.gestion_hotelera.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "habitaciones")
@Data
@NoArgsConstructor      // <-- AÑADIR ESTA LÍNEA
@AllArgsConstructor     // <-- AÑADIR ESTA LÍNEA
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false, unique = true)
    private String numeroHabitacion;

    @Enumerated(EnumType.STRING) // Guarda el ENUM como String en la BD (más legible)
    @Column(length = 20, nullable = false)
    private EstadoHabitacion estado;

    @Column(nullable = false)
    private BigDecimal precioPorNoche;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // --- RELACIONES ---

    // Relación con TipoHabitacion (Muchas habitaciones pueden ser de un tipo)
    @ManyToOne(fetch = FetchType.EAGER) // EAGER para que siempre traiga el tipo de habitación
    @JoinColumn(name = "tipo_habitacion_id", nullable = false)
    private TipoHabitacion tipoHabitacion;

    // Relación con Reservacion (Una habitación puede tener muchas reservas)
    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("habitacion-reservacion") // Referencia única para esta relación
    private List<Reservacion> reservaciones;

    // Relación con TareaLimpieza (Una habitación puede tener muchas tareas de limpieza)
    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("habitacion-limpieza") // Referencia única para esta relación
    private List<TareaLimpieza> tareasDeLimpieza;

    // --- ENUM para el estado ---
    public enum EstadoHabitacion {
        DISPONIBLE,
        OCUPADA,
        SUCIA,
        EN_LIMPIEZA,
        MANTENIMIENTO
    }
}



