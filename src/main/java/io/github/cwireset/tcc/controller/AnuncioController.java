package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.request.*;
import io.github.cwireset.tcc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    public List<Anuncio> listarAnuncios(){
        return anuncioService.listarAnuncios();
    }

    @GetMapping("/anunciantes/{idAnunciante}")
    public List<Anuncio> listarAnunciosDeUmAnuncianteEspecifico(
            @PathVariable
            @Valid
            @NotNull Long idAnunciante) throws Exception {
        return anuncioService.listarAnunciosDeUmAnuncianteEspecifico(idAnunciante);
    }

    @DeleteMapping("{idAnuncio}")
    public void excluirAnuncio(
           @PathVariable
           @Valid
           @NotNull Long idAnuncio) throws Exception {
        anuncioService.excluirAnuncio(idAnuncio);
    }
}
