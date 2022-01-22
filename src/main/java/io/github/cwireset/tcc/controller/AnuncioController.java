package io.github.cwireset.tcc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.request.CadastrarAnuncioRequest;
import io.github.cwireset.tcc.service.AnuncioService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/anuncios")
public class AnuncioController {

    @Autowired
    private AnuncioService anuncioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Anuncio anunciarImovel(@Valid @RequestBody CadastrarAnuncioRequest cadastrarAnuncioRequest) throws Exception {
        return anuncioService.anunciarImovel(cadastrarAnuncioRequest);
    }

    @GetMapping
    public Page<Anuncio> listarAnuncios(
            @PageableDefault(sort = "valorDiaria",
            direction = Sort.Direction.ASC,
            page = 0,
            size = 10) Pageable page){
        return anuncioService.listarAnuncios(page);
    }

    @GetMapping("/anunciantes/{idAnunciante}")
    public Page<Anuncio> listarAnunciosDeUmAnuncianteEspecifico(
            @PathVariable
            @Valid
            @NotNull Long idAnunciante,
            @PageableDefault(sort = "valorDiaria",
                    direction = Sort.Direction.ASC,
                    page = 0,
                    size = 10) Pageable page) throws Exception {
        return anuncioService.listarAnunciosDeUmAnuncianteEspecifico(idAnunciante, page);
    }

    @DeleteMapping("{idAnuncio}")
    public void excluirAnuncio(
           @PathVariable
           @Valid
           @NotNull Long idAnuncio) throws Exception {
        anuncioService.excluirAnuncio(idAnuncio);
    }
}
