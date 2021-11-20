package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findAllBySolicitante(Usuario usuario);

    Optional<Reserva> findById(Long id);

    boolean findByPeriodoDataHoraInicialLessThanEqual(LocalDateTime data);

    boolean findByPeriodoDataHoraFinalGreaterThanEqual(LocalDateTime data);

    boolean findByPeriodoDataHoraInicialGreaterThanEqual(LocalDateTime data);

    boolean findByPeriodoDataHoraFinalLessThanEqual(LocalDateTime data);

    List<Reserva> findAllByAnuncioAnunciante(Usuario usuario);

    boolean existsBy();

}
