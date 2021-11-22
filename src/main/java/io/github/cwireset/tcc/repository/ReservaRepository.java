package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    Page<Reserva> findAllBySolicitante(Usuario usuario, Pageable pageable);

    Reserva findById(Long id);

    Page<Reserva> findAllByAnuncioAnunciante(Usuario usuario, Pageable pageable);

    boolean existsBy();

    boolean existsById(Long id);

    boolean existsByAnuncioAndPeriodoDataHoraInicialLessThanEqualAndPeriodoDataHoraFinalGreaterThanEqual(Anuncio anuncio, LocalDateTime data, LocalDateTime dataDois);

    boolean existsByAnuncioAndPeriodoDataHoraInicialGreaterThanEqualAndPeriodoDataHoraFinalLessThanEqual(Anuncio anuncio, LocalDateTime data, LocalDateTime dataDois);

    Page<Reserva> findAllBySolicitanteAndPeriodoDataHoraInicialGreaterThanEqualAndPeriodoDataHoraFinalLessThanEqual(Usuario usuario, LocalDateTime dataHoraInicial, LocalDateTime dataHoraFinal, Pageable pageable);
}
