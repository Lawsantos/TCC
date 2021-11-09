package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.exception.IdInvalidException;
import io.github.cwireset.tcc.request.CadastrarImovelRequest;
import io.github.cwireset.tcc.service.ImovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/imoveis")
public class ImovelController {

    @Autowired
    private ImovelService imovelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Imovel cadastrarImovel(@Valid @RequestBody CadastrarImovelRequest cadastrarImovelRequest) throws Exception {
        return imovelService.cadastrarImovel(cadastrarImovelRequest);
    }

    @GetMapping
    public List<Imovel> listarImoveis(){
        return imovelService.listarImovel();
    }

    @GetMapping("/proprietarios/{idProprietario}")
    public List<Imovel> listarImoveisPorProprietario(
            @PathParam("idProprietario")
            @Valid
            @NotNull Long idProprietario) throws Exception {
        return imovelService.listarImoveisPorProprietario(idProprietario);
    }

    @GetMapping("/{idImovel}")
    public Imovel buscarImovelPorId(
            @PathParam("id")
            @Valid
            @NotNull(message = "Campo obrigatório não informado. Favor informar o campo id.")Long id) throws IdInvalidException {
        return imovelService.buscarImovelPorId(id);
    }

    @DeleteMapping("/{idImovel}")
    public void deletarImovel(
            @PathParam("idImovel")
            @Valid
            @NotNull Long id) throws IdInvalidException {
        imovelService.deletarImovel(id);
    }

}