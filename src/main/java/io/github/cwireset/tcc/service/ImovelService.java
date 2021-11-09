package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.exception.*;
import io.github.cwireset.tcc.repository.*;
import io.github.cwireset.tcc.request.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImovelService {

    @Autowired
    private ImovelRepository imovelRepository;
    @Autowired
    private UsuarioService usuarioService;

    public Imovel cadastrarImovel(CadastrarImovelRequest cadastrarImovelRequest) throws Exception {

        Usuario usuario = usuarioService.buscarUsuarioPorId(cadastrarImovelRequest.getIdProprietario());
        Imovel imovel = new Imovel();
        imovel.setIdentificacao(cadastrarImovelRequest.getIdentificacao());
        imovel.setTipoImovel(cadastrarImovelRequest.getTipoImovel());
        imovel.setEndereco(cadastrarImovelRequest.getEndereco());
        imovel.setProprietario(usuario);
        imovel.setCaracteristicas(cadastrarImovelRequest.getCaracteristicas());
        return imovelRepository.save(imovel);
    }

    public List<Imovel> listarImovel() {
        return imovelRepository.findAll();
    }

    public List<Imovel> listarImoveisPorProprietario(Long idProprietario) throws Exception {
        return imovelRepository.findAllByProprietario(usuarioService.buscarUsuarioPorId(idProprietario));
    }

    public Imovel buscarImovelPorId(Long id) throws IdInvalidException {
        if(!imovelRepository.existsById(id)){
            throw new IdInvalidException("Imovel", id);
        }
        return imovelRepository.findById(id);
    }

    public void deletarImovel(Long id) throws IdInvalidException {
        Imovel imovel = buscarImovelPorId(id);
        if(!imovelRepository.existsById(id)){
            throw new IdInvalidException("Imovel", id);
        }
        imovelRepository.delete(imovel);
    }
}
