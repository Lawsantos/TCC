package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByCpf(String cpf);

    Usuario findById(Long id);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsById(Long id);

    boolean existsByEmailAndIdNot(String email, Long id);
}
