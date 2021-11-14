package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findAllBySolicitante(Usuario usuario);

    List<Reserva> findAllByAnunciante(Usuario usuario);
}
