package com.Hotel.gestion_hotelera.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reservaciones")
@Data
public class Reservacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_checkin", nullable = false)
    private LocalDateTime fechaCheckin;

    @Column(name = "fecha_checkout", nullable = false)
    private LocalDateTime fechaCheckout;

    @Column(name = "numero_huespedes", nullable = false)
    private Integer numeroHuespedes;

    @Column(name = "monto_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_reserva", length = 20, nullable = false)
    private EstadoReserva estadoReserva;

    @CreationTimestamp // Gestionado automáticamente por Hibernate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // Gestionado automáticamente por Hibernate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // --- RELACIONES ---

    // Muchas reservaciones pueden ser de un cliente (Usuario)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonBackReference("usuario-reservacion")
    private Usuario cliente;

    // Muchas reservaciones pueden ser para una habitación
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitacion_id", nullable = false)
    @JsonBackReference("habitacion-reservacion")
    private Habitacion habitacion;

    // Una reservación puede tener muchos pagos
    @OneToMany(mappedBy = "reservacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("reservacion-pago")
    private List<Pago> pagos;


    // --- ENUM para el estado de la reserva ---
    public enum EstadoReserva {
        PENDIENTE,
        CONFIRMADA,
        CANCELADA,
        CHECKED_IN, // El cliente ya está en el hotel
        CHECKED_OUT // El cliente ya se fue
    }
}