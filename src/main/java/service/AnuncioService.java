package service;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.exception.*;
import io.github.cwireset.tcc.repository.AnuncioRepository;
import io.github.cwireset.tcc.request.CadastrarAnuncioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

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
            throw new DataDuplicatedException("Anuncio", "IdImovel", String.valueOf(imovel.getId()));
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

    public Page<Anuncio> listarAnuncios(Pageable pageable) {
        return anuncioRepository.findAllByAtivoIsTrue(pageable);
    }

    public Page<Anuncio> listarAnunciosDeUmAnuncianteEspecifico(Long idAnunciante, Pageable pageable) throws Exception {

        if (!anuncioRepository.existsByAnuncianteAndAtivoIsTrue(usuarioService.buscarUsuarioPorId(idAnunciante))){
            throw new IdInvalidException("Anuncio", idAnunciante);
        }
        return anuncioRepository.findAllByAnuncianteAndAtivoIsTrue(usuarioService.buscarUsuarioPorId(idAnunciante), pageable);
    }

    public void excluirAnuncio(Long idAnuncio) throws Exception {

        Anuncio anuncio = buscarAnuncioPorId(idAnuncio);
        anuncio.setAtivo(false);
        anuncio.setImovel(null);
        anuncioRepository.save(anuncio);
    }

    public Anuncio buscarAnuncioPorId(Long id) throws IdInvalidException {

        if(!anuncioRepository.existsByIdAndAtivoIsTrue(id)){
            throw new IdInvalidException("Anuncio", id);
        }
        return anuncioRepository.findByIdAndAtivoIsTrue(id);
    }
}
