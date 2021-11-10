package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.*;
import io.github.cwireset.tcc.repository.UsuarioRepository;
import io.github.cwireset.tcc.request.AtualizarUsuarioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario salvarUsuario(Usuario usuario) throws Exception{
        if(usuarioRepository.existsByEmail(usuario.getEmail())){
           throw new DataDuplicatedException("Usuario", "E-Mail", usuario.getEmail());
        }

        if(usuarioRepository.existsByCpf(usuario.getCpf())){
            throw new DataDuplicatedException("Usuario", "CPF", usuario.getCpf());
        }
       return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuario() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarUsuarioPorId(Long id) throws Exception{
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

    public void atualizarUsuario(Long id, AtualizarUsuarioRequest atualizarUsuarioRequest) throws Exception {
        Usuario usuarioTemp = buscarUsuarioPorId(id);
        if(usuarioRepository.existsByEmailAndIdNot(atualizarUsuarioRequest.getEmail(), id)){
            throw new DataDuplicatedException("Usuario", "E-Mail", atualizarUsuarioRequest.getEmail());
        }

        usuarioTemp.setNome(atualizarUsuarioRequest.getNome());
        usuarioTemp.setEmail(atualizarUsuarioRequest.getEmail());
        usuarioTemp.setSenha(atualizarUsuarioRequest.getSenha());
        usuarioTemp.setDataNascimento(atualizarUsuarioRequest.getDataNascimento());
        usuarioTemp.setEndereco(atualizarUsuarioRequest.getEndereco());

        this.usuarioRepository.save(usuarioTemp);
    }
}
