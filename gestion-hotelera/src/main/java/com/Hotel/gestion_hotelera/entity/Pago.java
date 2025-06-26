package com.Hotel.gestion_hotelera.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor      // <-- AÑADIR ESTA LÍNEA
@AllArgsConstructor     // <-- AÑADIR ESTA LÍNEA
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", length = 20, nullable = false)
    private MetodoPago metodoPago;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", length = 20, nullable = false)
    private EstadoPago estadoPago;

    // --- RELACIONES ---

    // Muchos pagos pueden pertenecer a una reservación.
    // FetchType.LAZY es importante para no cargar la reservación completa a menos que se pida explícitamente.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservacion_id", nullable = false)
    @JsonBackReference("reservacion-pago") // Evita la recursión infinita al serializar a JSON
    private Reservacion reservacion;


    // --- ENUMs basados en tu diseño ---
    public enum MetodoPago {
        TARJETA_CREDITO,
        EFECTIVO,
        TRANSFERENCIA,
        YAPE_PLIN // ¡Excelente toque localizado!
    }

    public enum EstadoPago {
        COMPLETADO,
        PENDIENTE,
        FALLIDO,
        CANCELADO, // Es bueno tener este estado
        REEMBOLSADO
    }
}