package repository;

import io.github.cwireset.tcc.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByCpf(String cpf);  

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsById(Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    Usuario findById(Long id);

    Page<Usuario> findAll(Pageable pageable);
}
