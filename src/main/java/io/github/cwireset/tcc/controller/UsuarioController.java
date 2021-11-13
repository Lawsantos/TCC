package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.request.*;
import io.github.cwireset.tcc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
    public Usuario cadastroDeUsuario(@RequestBody @Valid Usuario usuario) throws Exception {
        return usuarioService.cadastroDeUsuario(usuario);
    }

    @GetMapping
    public List<Usuario> listarUsuario(){
       return usuarioService.listarUsuario();
    }

    @GetMapping("/{idUsuario}")
    public Usuario buscarUsuarioPorId(
            @PathVariable
            @Valid
            @NotNull(message = "Campo obrigatório não informado. Favor informar o campo id.")Long idUsuario) throws Exception {
        return usuarioService.buscarUsuarioPorId(idUsuario);
    }

    @GetMapping(path = "/cpf/{cpf}")
    public Usuario buscarUsuarioPorCpf(
            @PathVariable
            @Valid
            @NotNull(message = "Campo obrigatório não informado. Favor informar o campo cpf.")String cpf) throws Exception {
        return usuarioService.buscarUsuarioPorCpf(cpf);
    }

    @PutMapping(path = "/{id}")
    public Usuario atualizarUsuario(
            @PathVariable
            @Valid
            @NotBlank(message = "Campo obrigatório não informado. Favor informar o campo id.")Long id, @Valid @RequestBody AtualizarUsuarioRequest atualizarUsuarioRequest) throws Exception {
        return usuarioService.atualizarUsuario(id, atualizarUsuarioRequest);
    }
}
