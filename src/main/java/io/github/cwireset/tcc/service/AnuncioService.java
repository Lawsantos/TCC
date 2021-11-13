package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.DataDuplicatedException;
import io.github.cwireset.tcc.exception.IdInvalidException;
import io.github.cwireset.tcc.repository.AnuncioRepository;
import io.github.cwireset.tcc.request.CadastrarAnuncioRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnuncioService {

    @Autowired
    private AnuncioRepository anuncioRepository;

    @Autowired
    private ImovelService imovelService;

    @Autowired
    private UsuarioService usuarioService;

    public Anuncio anunciarImovel(CadastrarAnuncioRequest cadastrarAnuncioRequest) throws Exception {
        Imovel imovel = imovelService.buscarImovelPorId(cadastrarAnuncioRequest.getIdImovel());
        Usuario usuario = usuarioService.buscarUsuarioPorId(cadastrarAnuncioRequest.getIdAnunciante());

        if(anuncioRepository.existsByImovel(imovel)){
            throw new DataDuplicatedException("Anuncio", "IdImovel ", String.valueOf(imovel.getId()));
        }

        Anuncio anuncio = new Anuncio();
        anuncio.setImovel(imovel);
        anuncio.setAnunciante(usuario);
        anuncio.setTipoAnuncio(cadastrarAnuncioRequest.getTipoAnuncio());
        anuncio.setValorDiaria(cadastrarAnuncioRequest.getValorDiaria());
        anuncio.setFormasAceitas(cadastrarAnuncioRequest.getFormasAceitas());
        anuncio.setDescricao(cadastrarAnuncioRequest.getDescricao());
        anuncio.setAtivo(true);
        return anuncioRepository.save(anuncio);
    }

    public List<Anuncio> listarAnuncios() {
        return anuncioRepository.findByAtivoIsTrue();
    }

    public List<Anuncio> listarAnunciosDeUmAnuncianteEspecifico(Long idAnunciante) throws Exception {
        if (!anuncioRepository.existsByAnuncianteAndAtivoIsTrue(usuarioService.buscarUsuarioPorId(idAnunciante))){
            throw new IdInvalidException("Anuncio", idAnunciante);
        }
        return anuncioRepository.findAllByAnuncianteAndAtivoIsTrue(usuarioService.buscarUsuarioPorId(idAnunciante));
    }

    public void excluirAnuncio(Long idAnuncio) throws Exception {
        Anuncio anuncio = anuncioRepository.findByIdAndAtivoIsTrue(idAnuncio);
        if (!anuncioRepository.existsByIdAndAtivoIsTrue(idAnuncio)){
            throw new IdInvalidException("Anuncio", idAnuncio);
        }
        anuncio.setAtivo(false);
        anuncioRepository.save(anuncio);
    }
}
