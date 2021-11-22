package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ImovelRepository extends JpaRepository<Imovel, Integer> {

    Page<Imovel> findAllByProprietarioAndAtivoIsTrue(Pageable pageable, Usuario usuario);

    boolean existsByIdAndAtivoIsTrue(Long id);

    Imovel findByIdAndAtivoIsTrue(Long id);

    Page<Imovel> findAllByAndAtivoIsTrue(Pageable pageable);
}
