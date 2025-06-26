package com.Hotel.gestion_hotelera.service;

import com.Hotel.gestion_hotelera.entity.Pago;
import com.Hotel.gestion_hotelera.entity.Reservacion;
import com.Hotel.gestion_hotelera.repository.PagoRepository;
import com.Hotel.gestion_hotelera.repository.ReservacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private ReservacionRepository reservacionRepository; // Necesario para asociar el pago

    @Transactional(readOnly = true)
    public List<Pago> findPagosByReservacionId(Long reservacionId) {
        return pagoRepository.findByReservacionId(reservacionId);
    }

    @Transactional(readOnly = true)
    public Optional<Pago> findById(Long id) {
        return pagoRepository.findById(id);
    }

    @Transactional
    public Pago createPago(Long reservacionId, Pago pago) {
        // 1. Buscar la reservación para asegurarse de que existe
        Reservacion reservacion = reservacionRepository.findById(reservacionId)
                .orElseThrow(() -> new RuntimeException("No se encontró la reservación con ID: " + reservacionId));

        // 2. Asociar el pago con la reservación encontrada
        pago.setReservacion(reservacion);

        // 3. Establecer la fecha de pago al momento de la creación
        pago.setFechaPago(LocalDateTime.now());

        // (Opcional) Se podría añadir lógica aquí. Ej: si la reserva se paga por completo,
        // cambiar el estado de la reserva a 'CONFIRMADA'.

        return pagoRepository.save(pago);
    }

    @Transactional
    public Optional<Pago> updateEstadoPago(Long pagoId, Pago.EstadoPago nuevoEstado) {
        return pagoRepository.findById(pagoId).map(pago -> {
            pago.setEstadoPago(nuevoEstado);
            return pagoRepository.save(pago);
        });
    }

    // Nota: Generalmente, los registros de pago no se eliminan (DELETE) por razones
    // de auditoría y contabilidad. En su lugar, se "cancelan" o "reembolsan"
    // cambiando su estado. Por eso no incluimos un método delete.
}