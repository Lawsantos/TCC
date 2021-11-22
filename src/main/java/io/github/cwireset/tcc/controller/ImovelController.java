package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.request.*;
import io.github.cwireset.tcc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/imoveis")
public class ImovelController {

    @Autowired
    private ImovelService imovelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Imovel cadastrarDeImovel(@Valid @RequestBody CadastrarImovelRequest cadastrarImovelRequest) throws Exception {
        return imovelService.cadastrarDeImovel(cadastrarImovelRequest);
    }

    @GetMapping
    public Page<Imovel> listarImoveis(@PageableDefault(sort = "identificacao",
            direction = Sort.Direction.ASC,
            page = 0,
            size = 10) Pageable page){
        return imovelService.listarImovel(page);
    }

    @GetMapping("/proprietarios/{idProprietario}")
    public Page<Imovel> listarImoveisDeUmProprietarioEspecifico(
            @PathVariable
            @Valid
            @NotNull
            Long idProprietario, @PageableDefault(sort = "identificacao",
            direction = Sort.Direction.ASC,
            page = 0,
            size = 10) Pageable page) throws Exception {
        return imovelService.listarImoveisDeUmProprietarioEspecifico(page, idProprietario);
    }

    @GetMapping("/{idImovel}")
    public Imovel buscarImovelPorId(
            @PathVariable
            @Valid
            @NotNull(message = "Campo obrigatório não informado. Favor informar o campo id.")Long idImovel) throws Exception {
        return imovelService.buscarImovelPorId(idImovel);
    }

    @DeleteMapping("/{idImovel}")
    public void excluirImovel(
            @PathVariable
            @Valid
            @NotNull Long idImovel) throws Exception {
        imovelService.excluirImovel(idImovel);
    }

}
