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
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reserva realizarReserva(@Valid @RequestBody CadastrarReservaRequest cadastrarReservaRequest) throws Exception{
        return reservaService.realizarReserva(cadastrarReservaRequest);
    }

    @GetMapping("/solicitantes/{idSolicitante}")
    public List<Reserva> listarReservaDeUmSolicitanteEspecifico(
            @PathVariable
            @Valid
            @NotNull Long idSolicitante) throws Exception {
        return reservaService.listarReservaDeUmSolicitanteEspecifico(idSolicitante);
    }

    @GetMapping("/anuncios/anunciantes/{idAnunciante}")
    public List<Reserva> listarReservaDeUmAnuncianteEspecifico(
            @PathVariable
            @Valid
            @NotNull Long idAnunciante) throws Exception {
        return reservaService.listarReservaDeUmAnuncianteEspecifico(idAnunciante);
    }

    @PutMapping("/{idReserva}/pagamentos")
    public void pagarReserva(Long idReserva){
        reservaService.pagarReserva(idReserva);
    }

    @PutMapping("/{idReserva}/pagamentos/cancelar")
    public void cancelarReserva(Long idReserva){
        reservaService.cancelarReserva(idReserva);
    }

    @PutMapping("/{idReserva}/pagamentos/estornar")
    public void estornarReserva(Long idReserva){
        reservaService.estornarReserva(idReserva);
    }

}
