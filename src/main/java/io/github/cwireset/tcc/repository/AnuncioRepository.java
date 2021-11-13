package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, Integer> {

    boolean existsById(Long idAnuncio);

    Anuncio findById(Long idAnuncio);

    boolean existsByImovel(Imovel imovel);

    List<Anuncio> findByAtivoIsTrue();

    List<Anuncio> findAllByAnuncianteAndAtivoIsTrue(Usuario usuario);

    Anuncio findByIdAndAtivoIsTrue(Long idAnuncio);

    boolean existsByIdAndAtivoIsTrue(Long idAnuncio);

    boolean existsByAnuncianteAndAtivoIsTrue(Usuario usuario);
}
