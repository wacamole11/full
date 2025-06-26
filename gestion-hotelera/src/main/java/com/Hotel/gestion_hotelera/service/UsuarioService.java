package com.Hotel.gestion_hotelera.service;

import com.Hotel.gestion_hotelera.entity.Usuario;
import com.Hotel.gestion_hotelera.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        // Validación para evitar emails duplicados
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalStateException("El email '" + usuario.getEmail() + "' ya está en uso.");
        }
        // La contraseña se guarda tal como viene, sin cifrar.
        return usuarioRepository.save(usuario);
    }

    /**
     * Método de login simple y sin seguridad.
     * Busca un usuario por email y compara la contraseña en texto plano.
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> login(String email, String password) {
        return usuarioRepository.findByEmail(email)
                .filter(usuario -> usuario.getPassword().equals(password));
    }

    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public Optional<Usuario> update(Long id, Usuario usuarioDetails) {
        return usuarioRepository.findById(id).map(existingUser -> {
            existingUser.setNombreCompleto(usuarioDetails.getNombreCompleto());
            existingUser.setEmail(usuarioDetails.getEmail());
            existingUser.setTelefono(usuarioDetails.getTelefono());
            existingUser.setRol(usuarioDetails.getRol());
            // Opcional: permitir cambiar la contraseña
            if (usuarioDetails.getPassword() != null && !usuarioDetails.getPassword().isEmpty()) {
                existingUser.setPassword(usuarioDetails.getPassword());
            }
            return usuarioRepository.save(existingUser);
        });
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
}