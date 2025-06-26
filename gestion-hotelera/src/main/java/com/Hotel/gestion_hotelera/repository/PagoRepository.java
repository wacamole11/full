package com.Hotel.gestion_hotelera.repository;

import com.Hotel.gestion_hotelera.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    /**
     * Busca todos los pagos asociados a un ID de reservación específico.
     * Spring Data JPA crea la consulta automáticamente a partir del nombre del método.
     * "findBy" + "Reservacion" (el campo en la entidad Pago) + "Id" (la propiedad del campo Reservacion)
     * @param reservacionId el ID de la reservación para buscar pagos.
     * @return una lista de pagos asociados a esa reservación.
     */
    List<Pago> findByReservacionId(Long reservacionId);
}