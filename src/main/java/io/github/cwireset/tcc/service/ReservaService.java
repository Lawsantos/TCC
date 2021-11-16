package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.exception.*;
import io.github.cwireset.tcc.repository.*;
import io.github.cwireset.tcc.request.*;
import io.github.cwireset.tcc.response.DadosAnuncioResponse;
import io.github.cwireset.tcc.response.DadosSolicitanteResponse;
import io.github.cwireset.tcc.response.InformacaoReservaResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AnuncioService anuncioService;

    public InformacaoReservaResponse realizarReserva(CadastrarReservaRequest cadastrarReservaRequest) throws Exception {
        InformacaoReservaResponse reservaResponse = new InformacaoReservaResponse();

        Usuario usuarioReserva = usuarioService.buscarUsuarioPorId(cadastrarReservaRequest.getIdSolicitante());
        Anuncio anuncioReserva = anuncioService.buscarAnuncioPorId(cadastrarReservaRequest.getIdAnuncio());
        BigDecimal total = anuncioReserva.getValorDiaria().multiply(BigDecimal.valueOf(calculoDiarias(cadastrarReservaRequest.getPeriodo())));

        if(usuarioReserva.getId().equals(anuncioReserva.getAnunciante().getId())){
            throw new FieldInvalid("O solicitante de uma reserva não pode ser o próprio anunciante.");
        }
        //reservaResponse.setIdReserva();
        reservaResponse.setSolicitante(
            new DadosSolicitanteResponse(
                usuarioReserva.getId(), usuarioReserva.getNome()));
        reservaResponse.setQuantidadePessoas(cadastrarReservaRequest.getQuantidadePessoas());
        reservaResponse.setAnuncio(
            new DadosAnuncioResponse(
                anuncioReserva.getId(),
                anuncioReserva.getImovel(),
                anuncioReserva.getAnunciante(),
                anuncioReserva.getFormasAceitas(),
                anuncioReserva.getDescricao()));
        reservaResponse.setPeriodo(horasSobrescritas(cadastrarReservaRequest.getPeriodo()));
        reservaResponse.setPagamento(
                new Pagamento(total,
                        null,
                        StatusPagamento.PENDENTE));

        Reserva reserva = new Reserva();
        reserva.setSolicitante(usuarioReserva);
        reserva.setAnuncio(anuncioReserva);
        reserva.setPeriodo(reservaResponse.getPeriodo());
        reserva.setQuantidadePessoas(reservaResponse.getQuantidadePessoas());
        reserva.setDataHoraReserva(LocalDateTime.now());
        reserva.setPagamento(reservaResponse.getPagamento());

        reservaRepository.save(reserva);
        return reservaResponse;
    }

    public List<Reserva> listarReservaDeUmSolicitanteEspecifico(Long idSolicitante) throws Exception {
        return reservaRepository.findAllBySolicitante(usuarioService.buscarUsuarioPorId(idSolicitante));
    }

    public List<Reserva> listarReservaDeUmAnuncianteEspecifico(Long idAnunciante) throws Exception {
        return reservaRepository.findAllByAnuncio(usuarioService.buscarUsuarioPorId(idAnunciante));
    }

    public void pagarReserva(Long idReserva) {
    }

    public void cancelarReserva(Long idReserva) {
    }

    public void estornarReserva(Long idReserva) {
    }

    public Long calculoDiarias(@NotNull Periodo periodo) throws Exception {

        Long numeroDiarias = periodo.getDataHoraInicial().toLocalDate().until(periodo.getDataHoraFinal().toLocalDate(), ChronoUnit.DAYS);

        if(numeroDiarias < 0){
            throw new FieldInvalid("Período inválido! A data final da reserva precisa ser maior do que a data inicial.");
        }

        if(numeroDiarias < 1){
            throw new FieldInvalid("Período inválido! O número mínimo de diárias precisa ser maior ou igual à 1.");
        }

        return numeroDiarias;
    }

    public Periodo horasSobrescritas(Periodo periodo){

        LocalDate dataInicial = periodo.getDataHoraInicial().toLocalDate();
        LocalDate dataFinal = periodo.getDataHoraFinal().toLocalDate();

        LocalTime horaReservaInicial = LocalTime.of(14, 00, 00);
        LocalTime horaReservaFinal = LocalTime.of(12, 00, 00);

        LocalDateTime periodoTempIni = dataInicial.atTime(horaReservaInicial);
        LocalDateTime periodoTempFim = dataFinal.atTime(horaReservaFinal);

        Periodo periodoSobrescrito = new Periodo(periodoTempIni, periodoTempFim);

        return (periodoSobrescrito);
    }
}
