package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.repository.*;
import io.github.cwireset.tcc.request.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Reserva realizarReserva(CadastrarReservaRequest cadastrarReservaRequest) {

        Reserva reserva = new Reserva();
        return reservaRepository.save(reserva);
    }

    public List<Reserva> listarReservaDeUmSolicitanteEspecifico(Long idSolicitante) throws Exception {
        return reservaRepository.findAllBySolicitante(usuarioService.buscarUsuarioPorId(idSolicitante));
    }

    public List<Reserva> listarReservaDeUmAnuncianteEspecifico(Long idAnunciante) throws Exception {
        return reservaRepository.findAllByAnunciante(usuarioService.buscarUsuarioPorId(idAnunciante));
    }

    public void pagarReserva(Long idReserva) {
    }

    public void cancelarReserva(Long idReserva) {
    }

    public void estornarReserva(Long idReserva) {
    }
}
