package com.Hotel.gestion_hotelera.controller;

import com.Hotel.gestion_hotelera.entity.Usuario;
import com.Hotel.gestion_hotelera.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
// Cambia 4200 por 3000
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    private Map<String, Object> convertirUsuarioAMapSeguro(Usuario usuario) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("id", usuario.getId());
        respuesta.put("nombreCompleto", usuario.getNombreCompleto());
        respuesta.put("email", usuario.getEmail());
        respuesta.put("telefono", usuario.getTelefono());
        respuesta.put("rol", usuario.getRol());
        return respuesta;
    }

    // POST /api/usuarios/register -> Registra un nuevo cliente
    @PostMapping("/register")
    public ResponseEntity<Object> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            usuario.setRol(Usuario.Rol.CLIENTE);
            Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
            return new ResponseEntity<>(convertirUsuarioAMapSeguro(nuevoUsuario), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // POST /api/usuarios/login -> Inicia sesión
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        return usuarioService.login(email, password)
                .map(usuarioEncontrado -> {
                    // --- ESTA ES LA LÍNEA CORREGIDA ---
                    // En lugar de ResponseEntity.ok(), creamos el ResponseEntity explícitamente.
                    Map<String, Object> responseBody = convertirUsuarioAMapSeguro(usuarioEncontrado);
                    return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Credenciales incorrectas");
                    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
                });
    }

    // GET /api/usuarios -> Obtiene todos los usuarios
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();
        List<Map<String, Object>> respuesta = usuarios.stream()
                .map(this::convertirUsuarioAMapSeguro)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuesta);
    }

    // GET /api/usuarios/{id} -> Obtiene un usuario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUsuarioById(@PathVariable Long id) {
        return usuarioService.findById(id)
                .map(usuario -> ResponseEntity.ok(convertirUsuarioAMapSeguro(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/usuarios/{id} -> Actualiza un usuario
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetails) {
        return usuarioService.update(id, usuarioDetails)
                .map(usuarioActualizado -> ResponseEntity.ok(convertirUsuarioAMapSeguro(usuarioActualizado)))
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/usuarios/{id} -> Elimina un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        if (usuarioService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}