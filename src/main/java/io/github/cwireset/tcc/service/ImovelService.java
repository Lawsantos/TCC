package io.github.cwireset.tcc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import io.github.cwireset.tcc.exception.IdInvalidException;
import io.github.cwireset.tcc.exception.NoDeleteException;
import io.github.cwireset.tcc.repository.AnuncioRepository;
import io.github.cwireset.tcc.repository.ImovelRepository;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.request.CadastrarImovelRequest;

import javax.validation.Valid;

@Service
public class ImovelService {

    @Autowired
    private ImovelRepository imovelRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AnuncioRepository anuncioRepository;

    public Imovel cadastrarDeImovel(CadastrarImovelRequest cadastrarImovelRequest) throws Exception {

        @Valid Usuario usuario = usuarioService.buscarUsuarioPorId(cadastrarImovelRequest.getIdProprietario());
        Imovel imovel = new Imovel();

        imovel.setIdentificacao(cadastrarImovelRequest.getIdentificacao());
        imovel.setTipoImovel(cadastrarImovelRequest.getTipoImovel());
        imovel.setEndereco(cadastrarImovelRequest.getEndereco());
        imovel.getEndereco().setCep(cadastrarImovelRequest.getEndereco().getCep());
        imovel.getEndereco().setBairro(cadastrarImovelRequest.getEndereco().getBairro());
        imovel.getEndereco().setCidade(cadastrarImovelRequest.getEndereco().getCidade());
        imovel.getEndereco().setEstado(cadastrarImovelRequest.getEndereco().getEstado());
        imovel.getEndereco().setLogradouro(cadastrarImovelRequest.getEndereco().getLogradouro());
        imovel.getEndereco().setNumero(cadastrarImovelRequest.getEndereco().getNumero());
        imovel.getEndereco().setComplemento(cadastrarImovelRequest.getEndereco().getComplemento());
        imovel.setProprietario(usuario);
        imovel.setCaracteristicas(cadastrarImovelRequest.getCaracteristicas());
        imovel.setAtivo(true);
        return imovelRepository.save(imovel);
    }

    public Page<Imovel> listarImovel(Pageable pageable) {
        return imovelRepository.findAllByAndAtivoIsTrue(pageable);
    }

    public Page<Imovel> listarImoveisDeUmProprietarioEspecifico(Pageable pageable, Long idProprietario) throws Exception {

        if (!imovelRepository.existsByIdAndAtivoIsTrue(idProprietario)){
            throw new IdInvalidException("Imovel", idProprietario);
        }
        return imovelRepository.findAllByProprietarioAndAtivoIsTrue(pageable, usuarioService.buscarUsuarioPorId(idProprietario));
    }

    public Imovel buscarImovelPorId(Long id) throws Exception {

        if(!imovelRepository.existsByIdAndAtivoIsTrue(id)){
            throw new IdInvalidException("Imovel", id);
        }
        return imovelRepository.findByIdAndAtivoIsTrue(id);
    }

    public void excluirImovel(Long id) throws Exception {

        Imovel imovel = buscarImovelPorId(id);

        if(anuncioRepository.existsByImovelAndAtivoIsTrue(imovel)){
            throw new NoDeleteException("Não é possível excluir um imóvel que possua um anúncio.");
        }
        imovel.setAtivo(false);
        imovelRepository.save(imovel);
    }
}
