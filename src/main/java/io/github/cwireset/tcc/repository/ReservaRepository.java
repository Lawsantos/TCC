package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.response.InformacaoReservaResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findAllBySolicitante(Usuario usuario);

    List<Reserva> findAllByAnuncio(Usuario usuario);

    InformacaoReservaResponse save(InformacaoReservaResponse informacaoReservaResponse);
}
