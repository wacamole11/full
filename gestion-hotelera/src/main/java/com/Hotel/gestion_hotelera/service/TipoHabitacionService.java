package com.Hotel.gestion_hotelera.service;

import com.Hotel.gestion_hotelera.entity.TipoHabitacion;
import com.Hotel.gestion_hotelera.repository.TipoHabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TipoHabitacionService {

    @Autowired
    private TipoHabitacionRepository tipoHabitacionRepository;

    @Transactional(readOnly = true)
    public List<TipoHabitacion> findAll() {
        return tipoHabitacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<TipoHabitacion> findById(Long id) {
        return tipoHabitacionRepository.findById(id);
    }

    @Transactional
    public TipoHabitacion save(TipoHabitacion tipoHabitacion) {
        // Aquí podrías agregar validaciones, por ejemplo, que el nombre no se repita
        return tipoHabitacionRepository.save(tipoHabitacion);
    }

    @Transactional
    public Optional<TipoHabitacion> update(Long id, TipoHabitacion tipoHabitacionDetails) {
        return tipoHabitacionRepository.findById(id).map(existingTipo -> {
            existingTipo.setNombre(tipoHabitacionDetails.getNombre());
            existingTipo.setDescripcion(tipoHabitacionDetails.getDescripcion());
            existingTipo.setCapacidad(tipoHabitacionDetails.getCapacidad());
            return tipoHabitacionRepository.save(existingTipo);
        });
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (tipoHabitacionRepository.existsById(id)) {
            tipoHabitacionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}