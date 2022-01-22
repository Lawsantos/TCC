package repository;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, Integer> {

    boolean existsByImovel(Imovel imovel);

    Page<Anuncio> findAllByAtivoIsTrue(Pageable pageable);

    Page<Anuncio> findAllByAnuncianteAndAtivoIsTrue(Usuario usuario, Pageable pageable);

    Anuncio findByIdAndAtivoIsTrue(Long idAnuncio);

    boolean existsByIdAndAtivoIsTrue(Long idAnuncio);

    boolean existsByAnuncianteAndAtivoIsTrue(Usuario usuario);

    boolean existsByImovelAndAtivoIsTrue(Imovel imovel);
}
