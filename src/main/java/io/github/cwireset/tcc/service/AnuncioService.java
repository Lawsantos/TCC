package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.DataDuplicatedException;
import io.github.cwireset.tcc.exception.IdInvalidException;
import io.github.cwireset.tcc.repository.AnuncioRepository;
import io.github.cwireset.tcc.request.CadastrarAnuncioRequest;
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
        return anuncioRepository.save(anuncio);
    }

    public List<Anuncio> listarAnuncios() {
        return anuncioRepository.findAll();
    }

    public List<Anuncio> listarAnunciosDeUmAnuncianteEspecifico(Long idAnunciante) throws Exception {
        return anuncioRepository.findAllByAnunciante(usuarioService.buscarUsuarioPorId(idAnunciante));
    }

    public void excluirAnuncio(Long idAnuncio) throws Exception {
        Anuncio anuncio = anuncioRepository.findById(idAnuncio);
        if (!anuncioRepository.existsById(idAnuncio)){
            throw new IdInvalidException("Anuncio", idAnuncio);
        }
        anuncioRepository.delete(anuncio);
    }
}
