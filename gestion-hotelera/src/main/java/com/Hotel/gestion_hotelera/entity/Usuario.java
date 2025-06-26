package com.Hotel.gestion_hotelera.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor; // AÑADIDA
import lombok.Data;
import lombok.NoArgsConstructor; // AÑADIDA
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor      // <-- AÑADIDA: Crea el constructor vacío. ¡ESTA ES LA CLAVE!
@AllArgsConstructor     // <-- AÑADIDA: Crea un constructor con todos los campos.
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // La contraseña se guardará como texto plano (SÓLO PARA DESARROLLO)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(length = 20)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- RELACIONES ---
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Reservacion> reservaciones;

    @OneToMany(mappedBy = "personalAsignado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<TareaLimpieza> tareasAsignadas;

    public enum Rol {
        ADMIN,
        RECEPCION,
        LIMPIEZA,
        CLIENTE
    }
}