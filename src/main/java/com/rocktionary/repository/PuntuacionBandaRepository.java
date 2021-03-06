package com.rocktionary.repository;

import com.rocktionary.domain.PuntuacionBanda;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the PuntuacionBanda entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PuntuacionBandaRepository extends JpaRepository<PuntuacionBanda, Long> {

    @Query("select puntuacion_banda from PuntuacionBanda puntuacion_banda where puntuacion_banda.user.login = ?#{principal.username}")
    PuntuacionBanda findByUserIsCurrentUser();

//
//    @Query("update PuntuacionBanda pb set pb.valoracion = ?1 where pb.user.login = ?#{principal.username}")
//    PuntuacionBanda updatePuntuacion(Integer newRating);

}
