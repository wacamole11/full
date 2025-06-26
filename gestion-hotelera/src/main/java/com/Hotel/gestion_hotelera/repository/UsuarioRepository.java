package com.Hotel.gestion_hotelera.repository;

import com.Hotel.gestion_hotelera.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Sigue siendo útil para el login y para verificar si un email ya existe
    Optional<Usuario> findByEmail(String email);

    // Útil para que un admin pueda buscar usuarios por rol
    List<Usuario> findByRol(Usuario.Rol rol);
}