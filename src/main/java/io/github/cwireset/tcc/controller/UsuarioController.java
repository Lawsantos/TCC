package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void salvarUsuario(@Valid @RequestBody Usuario usuario){
        usuarioService.salvarUsuario(usuario);
    }

    @GetMapping
    public List<Usuario> listarUsuario(){
       return usuarioService.listarUsuario();
    }

    @GetMapping("/{id}")
    public Usuario buscarUsuarioPorId(
            @PathParam("id")
            @Valid
            @NotNull(message = "Campo obrigatório não informado. Favor informar o campo id.")Long id){
        return usuarioService.buscarUsuarioPorId(id);
    }

    @GetMapping("/cpf/{cpf}")
    public Usuario buscarUsuarioPorCpf(
            @PathParam("cpf")
            @Valid
            @NotNull(message = "Campo obrigatório não informado. Favor informar o campo cpf.")String cpf){
        return usuarioService.buscarUsuarioPorCpf(cpf);
    }

    @PutMapping("/{id}")
    public void atualizarUsuario(
            @PathParam("id")
            @Valid
            @NotNull(message = "Campo obrigatório não informado. Favor informar o campo id.")Long id, Usuario usuario){
        this.usuarioService.atualizarUsuario(id, usuario);
    }
}
