package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.domain.FormaPagamento;
import io.github.cwireset.tcc.domain.Reserva;
import io.github.cwireset.tcc.request.CadastrarReservaRequest;
import io.github.cwireset.tcc.response.InformacaoReservaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.github.cwireset.tcc.service.ReservaService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InformacaoReservaResponse realizarReserva(@Valid @RequestBody CadastrarReservaRequest cadastrarReservaRequest) throws Exception{
        return reservaService.realizarReserva(cadastrarReservaRequest);
    }

    @GetMapping("/solicitantes/{idSolicitante}")
    public Page<Reserva> listarReservaDeUmSolicitanteEspecifico(
            @PathVariable
            @Valid
            @NotNull Long idSolicitante,
            @RequestParam(required = false, value = "dataHoraInicial")
            String dataHoraInicial,
            @RequestParam(required = false, value = "dataHoraFinal")
                    String dataHoraFinal,
            @PageableDefault(sort = "periodo.dataHoraFinal",
                    direction = Sort.Direction.DESC,
                    page = 0,
                    size = 10) Pageable page) throws Exception {

        return reservaService.listarReservaDeUmSolicitanteEspecifico(idSolicitante, dataHoraInicial, dataHoraFinal, page);
    }

    @GetMapping("/anuncios/anunciantes/{idAnunciante}")
    public Page<Reserva> listarReservaDeUmAnuncianteEspecifico(
            @PathVariable
            @Valid
            @NotNull Long idAnunciante,
            @PageableDefault(sort = "periodo.dataHoraFinal",
                    direction = Sort.Direction.DESC,
                    page = 0,
                    size = 10) Pageable page) throws Exception {
        return reservaService.listarReservaDeUmAnuncianteEspecifico(idAnunciante, page);
    }

    @PutMapping("/{idReserva}/pagamentos")
    @ResponseStatus(HttpStatus.CREATED)
    public void pagarReserva(@PathVariable Long idReserva, @PathVariable FormaPagamento formaPagamento) throws Exception {
        reservaService.pagarReserva(idReserva, formaPagamento);
    }

    @PutMapping("/{idReserva}/pagamentos/cancelar")
    public void cancelarReserva(@PathVariable Long idReserva) throws Exception {
        reservaService.cancelarReserva(idReserva);
    }

    @PutMapping("/{idReserva}/pagamentos/estornar")
    public void estornarReserva(@PathVariable Long idReserva) throws Exception {
        reservaService.estornarReserva(idReserva);
    }

}
