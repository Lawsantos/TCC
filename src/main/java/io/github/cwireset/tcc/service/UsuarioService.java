package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.exception.CpfInvalidException;
import io.github.cwireset.tcc.exception.DataDuplicatedException;
import io.github.cwireset.tcc.exception.IdInvalidException;
import io.github.cwireset.tcc.repository.UsuarioRepository;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.request.AtualizarUsuarioRequest;
import io.github.cwireset.tcc.utils.AvatarFeighClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private AvatarFeighClient avatarFeighClient;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastroDeUsuario(Usuario usuario) throws Exception {
        if(usuarioRepository.existsByEmail(usuario.getEmail())){
           throw new DataDuplicatedException("Usuario", "E-Mail", usuario.getEmail());
        }

        if(usuarioRepository.existsByCpf(usuario.getCpf())) {
            throw new DataDuplicatedException("Usuario", "CPF", usuario.getCpf());
        }

        try {
            usuario.setAvatar(avatarFeighClient.getAvatar().getLink());
        }catch (Exception e) {
            usuario.setAvatar("");
        }
        return usuarioRepository.save(usuario);
    }

    public Page<Usuario> listarUsuario(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public Usuario buscarUsuarioPorId(Long id) throws Exception {
        if(!usuarioRepository.existsById(id)){
            throw new IdInvalidException("Usuario", id);
        }
        return usuarioRepository.findById(id);
    }

    public Usuario buscarUsuarioPorCpf(String cpf) throws Exception {
        if(!usuarioRepository.existsByCpf(cpf)){
            throw new CpfInvalidException(cpf);
        }
        return usuarioRepository.findByCpf(cpf);
    }

    public Usuario atualizarUsuario(Long id, AtualizarUsuarioRequest atualizarUsuarioRequest) throws Exception {
        Usuario usuarioTemporario = buscarUsuarioPorId(id);

        if(usuarioRepository.existsByEmailAndIdNot(atualizarUsuarioRequest.getEmail(), id)){
            throw new DataDuplicatedException("Usuario", "E-Mail", atualizarUsuarioRequest.getEmail());
        }

        usuarioTemporario.setNome(atualizarUsuarioRequest.getNome());
        usuarioTemporario.setEmail(atualizarUsuarioRequest.getEmail());
        usuarioTemporario.setSenha(atualizarUsuarioRequest.getSenha());
        usuarioTemporario.setDataNascimento(atualizarUsuarioRequest.getDataNascimento());

        usuarioTemporario.getEndereco().setCep(atualizarUsuarioRequest.getEndereco().getCep());
        usuarioTemporario.getEndereco().setBairro(atualizarUsuarioRequest.getEndereco().getBairro());
        usuarioTemporario.getEndereco().setCidade(atualizarUsuarioRequest.getEndereco().getCidade());
        usuarioTemporario.getEndereco().setEstado(atualizarUsuarioRequest.getEndereco().getEstado());
        usuarioTemporario.getEndereco().setLogradouro(atualizarUsuarioRequest.getEndereco().getLogradouro());
        usuarioTemporario.getEndereco().setNumero(atualizarUsuarioRequest.getEndereco().getNumero());
        usuarioTemporario.getEndereco().setComplemento(atualizarUsuarioRequest.getEndereco().getComplemento());

        return usuarioRepository.save(usuarioTemporario);
    }

}
