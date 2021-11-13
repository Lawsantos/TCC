package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ImovelRepository extends JpaRepository<Imovel, Integer> {
    Imovel findById(Long id);

    boolean existsById(Long id);

    List<Imovel> findAllByProprietario(@javax.validation.Valid Usuario usuario);
}
