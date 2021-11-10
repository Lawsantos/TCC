package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, Integer> {
    List<Anuncio> findAllByAnunciante(Usuario usuario);

    boolean existsById(Long idAnuncio);

    Anuncio findById(Long idAnuncio);

    boolean existsByImovel(Imovel imovel);
}
