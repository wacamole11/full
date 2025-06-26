package com.Hotel.gestion_hotelera.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "tipos_habitacion")
@Data
@NoArgsConstructor      // <-- AÑADIR ESTA LÍNEA
@AllArgsConstructor     // <-- AÑADIR ESTA LÍNEA
public class TipoHabitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private Integer capacidad; // Usar Integer en lugar de int para permitir nulos si fuera necesario

    // Una tipo de habitación puede estar en muchas habitaciones
    @OneToMany(mappedBy = "tipoHabitacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // Evita que se serialicen las habitaciones al pedir un tipo, previene bucles
    @ToString.Exclude // Evita problemas de recursividad en el método toString de Lombok
    private List<Habitacion> habitaciones;
}